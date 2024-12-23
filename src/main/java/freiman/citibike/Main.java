package freiman.citibike;

import freiman.citibike.json.Station;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        StationServiceFactory factory = new StationServiceFactory();
        StationService service = factory.getService();
        Map<String, Station> stations = factory.merge(service);
        StationFinder stationFinder = new StationFinder(stations);
        double lon = -73.97121214;
        double lat = 40.744219;

        Station beverly = stations.get("1817433126359581672");
        Station FDR = stations.get("66dc7659-0aca-11e7-82f6-3863bb44ef7c");
        double disToBeverly = Math.sqrt(((lat - beverly.lat) * (lat - beverly.lat)) +
                ((lon - beverly.lon) * (lon - beverly.lon)));
        double disToFDR = Math.sqrt(((lat - FDR.lat) * (lat - FDR.lat)) +
                ((lon - FDR.lon) * (lon - FDR.lon)));
        System.out.println("B: " + disToBeverly);
        System.out.println(beverly.num_docks_available);
        System.out.println("F: " + disToFDR);
        System.out.println(FDR.num_docks_available);





        Station closestStation = stationFinder.closestStation(lon, lat, true);
        System.out.println(closestStation.name);
    }
}
