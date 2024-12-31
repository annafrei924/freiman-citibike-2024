package freiman.citibike.map;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import freiman.citibike.StationFinder;
import freiman.citibike.aws.CitibikeRequest;
import freiman.citibike.aws.CitibikeRequestHandler;
import freiman.citibike.aws.CitibikeResponse;
import freiman.citibike.aws.Coordinate;
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
import retrofit2.Call;
import retrofit2.Response;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class MapController {
    private JXMapViewer mapViewer;
    private BiConsumer<Double, Double> onStartPointSelected;
    private BiConsumer<Double, Double> onEndPointSelected;
    private boolean startClicked = false;
    private GeoPosition startLocation;
    private GeoPosition endLocation;
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
                    startLocation = mapViewer.convertPointToGeoPosition(e.getPoint());
                    startClicked = true;
                    if (onStartPointSelected != null) {
                        onStartPointSelected.accept(startLocation.getLatitude(), startLocation.getLongitude());
                    }
                } else {
                    endLocation = mapViewer.convertPointToGeoPosition(e.getPoint());
                    if (onEndPointSelected != null) {
                        onEndPointSelected.accept(endLocation.getLatitude(), endLocation.getLongitude());
                    }
                }
            }
        });
        return mapViewer;
    }

    public CitibikeRequest writeToJson() {
        // Create Coordinate objects
        Coordinate from = new Coordinate();
        from.lat = startLocation.getLatitude();
        from.lon = startLocation.getLongitude();

        Coordinate to = new Coordinate();
        to.lat = endLocation.getLatitude();
        to.lon = endLocation.getLongitude();

        CitibikeRequest request = new CitibikeRequest();
        request.from = from;
        request.to = to;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("request.json")) {
            gson.toJson(request, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return request;

    }

    public CitibikeResponse getLambda(CitibikeRequest request) {
        CitibikeResponse citibikeResponse = null;
        StationService api = StationServiceFactory.getLambda();
        Call<CitibikeResponse> call = api.postCitibikeRequest(request);
        try {
            Response<CitibikeResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                citibikeResponse = response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(citibikeResponse.start.name);
        return citibikeResponse;
    }


    public void drawRoute() {
        CitibikeResponse response = getLambda(writeToJson());
        Station startStation = response.start;
        Station endStation = response.end;

        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypoints = Set.of(
                new DefaultWaypoint(startLocation),
                new DefaultWaypoint(endLocation),
                new DefaultWaypoint(startStation.lat, startStation.lon),
                new DefaultWaypoint(endStation.lat, endStation.lon)
        );
        waypointPainter.setWaypoints(waypoints);



        track.add(startLocation);
        track.add(new GeoPosition(startStation.lat, startStation.lon));
        track.add(new GeoPosition(endStation.lat, endStation.lon));
        track.add(endLocation);


        RoutePainter routePainter = new RoutePainter(track);

        List<Painter<JXMapViewer>> painters = List.of(
                routePainter,
                waypointPainter
        );

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);

        mapViewer.zoomToBestFit(
                Set.of(
                        startLocation,
                        new GeoPosition(startStation.lat, startStation.lon),
                        new GeoPosition(endStation.lat, endStation.lon),
                        endLocation
                ),
                1.0
        );
        mapViewer.repaint();
    }

    public void clear() {
        startClicked = false;
        startLocation = null;
        endLocation = null;
//        startStation = null;
//        endStation = null;
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
