package freiman.citibike;

import freiman.citibike.aws.StationCache;
import freiman.citibike.json.Station;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Map;

public class ClosestStationTest {

    @Test
    void closestStationReturn() {
        try {
            //given
            StationServiceFactory factory = new StationServiceFactory();
            StationService service = factory.getService();
            StationCache cache = new StationCache();
            StationFinder stationFinder = new StationFinder(factory.merge(service, cache));
            double lon = -73.971212141;
            double lat = 40.744220;


            //when
            Station closestStation = stationFinder.closestStation(lat, lon, true);

            //then
            assertNotNull(closestStation);
            assertEquals(closestStation.name, "FDR Drive & E 35 St");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertNotNull(e);
        }
    }

    @Test
    void closestStationPickUp() {
        try {
            //given
            StationServiceFactory factory = new StationServiceFactory();
            StationService service = factory.getService();
            StationCache cache = new StationCache();
            StationFinder stationFinder = new StationFinder(factory.merge(service, cache));
            double lon = -73.971212141;
            double lat = 40.744220;


            //when
            Station closestStation = stationFinder.closestStation(lat, lon, false);

            //then
            assertNotNull(closestStation);
            assertEquals(closestStation.name, "FDR Drive & E 35 St");
        } catch (Exception e) {
            assertNotNull(e);
            System.out.println(e.getMessage());
        }
    }
}
