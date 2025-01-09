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
        
            //given
            double lon = -73.971212141;
            double lat = 40.744220;

            StationCache cache = new StationCache();
            StationFinder stationFinder = new StationFinder(cache);

            //when
            Station closestStation = stationFinder.closestStation(lat, lon, true);

            //then
            assertNotNull(closestStation);
            assertEquals(closestStation.name, "FDR Drive & E 35 St");
        
    }

    @Test
    void closestStationPickUp() {

            //given
            StationCache cache = new StationCache();
            StationFinder stationFinder = new StationFinder(cache);
            double lon = -73.971212141;
            double lat = 40.744220;

            //when
            Station closestStation = stationFinder.closestStation(lat, lon, false);

            //then
            assertNotNull(closestStation);
            assertEquals(closestStation.name, "FDR Drive & E 35 St");

    }
}
