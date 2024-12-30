package freiman.citibike.map;


import freiman.citibike.StationFinder;
import freiman.citibike.aws.CitibikeRequestHandler;
import freiman.citibike.json.Station;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class MapController {
    private JXMapViewer mapViewer;
    private BiConsumer<Double, Double> onStartPointSelected;
    private BiConsumer<Double, Double> onEndPointSelected;
    private boolean startClicked = false;
    private GeoPosition startPosition;
    private GeoPosition endPosition;
    private Station startStation;
    private Station endStation;
    private List<GeoPosition> track = new ArrayList<>();
    private Set<Waypoint> waypoints;

    public JXMapViewer createMap() {
        mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        tileFactory.setThreadPoolSize(8);

        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(new GeoPosition(40.76, -73.98));

        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                if (!startClicked) {
                    startPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                    startClicked = true;
                    if (onStartPointSelected != null) {
                        onStartPointSelected.accept(startPosition.getLatitude(), startPosition.getLongitude());
                    }
                } else {
                    endPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                    if (onEndPointSelected != null) {
                        onEndPointSelected.accept(endPosition.getLatitude(), endPosition.getLongitude());
                    }
                }
            }
        });
        return mapViewer;
    }

    public void getClosestStation() {
        CitibikeRequestHandler handler = new CitibikeRequestHandler();
        StationServiceFactory factory = new StationServiceFactory();
        StationService service = factory.getService();
        StationFinder stationFinder = new StationFinder(factory.merge(service));
        startStation = stationFinder.closestStation(startPosition.getLatitude(),
                startPosition.getLongitude(), false);
        endStation = stationFinder.closestStation(endPosition.getLatitude(),
                endPosition.getLongitude(), true);
    }

    public void draw() {
        getClosestStation();
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypoints = Set.of(
                new DefaultWaypoint(startPosition),
                new DefaultWaypoint(endPosition),
                new DefaultWaypoint(startStation.lat, startStation.lon),
                new DefaultWaypoint(endStation.lat, endStation.lon)
        );


        track.add(startPosition);
        track.add(mapViewer.convertPointToGeoPosition(new Point2D.Double(startStation.lon,
                startStation.lat)));
        track.add(mapViewer.convertPointToGeoPosition(new Point2D.Double(endStation.lon,
                endStation.lat)));
        track.add(endPosition);


        RoutePainter routePainter = new RoutePainter(track);

        waypointPainter.setWaypoints(waypoints);
        List<Painter<JXMapViewer>> painters = List.of(
                routePainter,
                waypointPainter
        );

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);

//        mapViewer.zoomToBestFit(
//                Set.of(from, startStation, endStation, to),
//                1.0
//        );
    }

    public void clear() {
        startClicked = false;
        startPosition = null;
        endPosition = null;
        startStation = null;
        endStation = null;
        track.clear();
        waypoints = Set.of();
        mapViewer.setOverlayPainter(null); // Clear overlay painters
        mapViewer.repaint();
        System.out.println("Cleared all points and overlays.");
    }

    public void setOnStartPointSelected(BiConsumer<Double, Double> listener) {
        this.onStartPointSelected = listener;
    }

    public void setOnEndPointSelected(BiConsumer<Double, Double> listener) {
        this.onEndPointSelected = listener;
    }
}
