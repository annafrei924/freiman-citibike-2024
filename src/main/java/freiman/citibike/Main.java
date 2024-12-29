package freiman.citibike;

import freiman.citibike.json.Station;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import freiman.citibike.json.Stations;
import freiman.citibike.map.MapController;
import freiman.citibike.map.MapFrame;
import io.reactivex.rxjava3.core.Single;
import org.jxmapviewer.JXMapViewer;


public class Main {
    public static void main(String[] args) {
        StationServiceFactory factory = new StationServiceFactory();
        StationService service = factory.getService();
        StationFinder stationFinder = new StationFinder(factory.merge(service));
        double lon = -73.971212141;
        double lat = 40.744220;


        Station start = stationFinder.closestStation(lat, lon, true);

        MapController mapController = new MapController();
        MapFrame frame = new MapFrame(mapController);
        frame.setVisible(true);
    }

}
