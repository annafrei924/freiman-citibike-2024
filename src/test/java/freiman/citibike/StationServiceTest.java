package freiman.citibike;

import freiman.citibike.aws.StationCache;
import freiman.citibike.json.Station;
import freiman.citibike.json.StationService;
import freiman.citibike.json.StationServiceFactory;
import freiman.citibike.json.Stations;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StationServiceTest {

    @Test
    void stationInfo() {
        // Given
        StationService service = new StationServiceFactory().getService();

        // When
        Stations collection = service.stationInfo().blockingGet();

        assertNotNull(collection.data.stations[0].station_id);
        assertNotNull(collection.data.stations[0].name);
        assertNotEquals(0, collection.data.stations[0].lon);
        assertNotEquals(0, collection.data.stations[0].lat);

    }


    @Test
    void stationStatus() {
        // Given
        StationService service = new StationServiceFactory().getService();

        // When
        Stations collection = service.stationStatus().blockingGet();

        //then
        assertNotNull(collection.data.stations[2].station_id);
        //assertNotEquals(0, collection.data.stations[2].num_bikes_available);
        assertNotEquals(0, collection.data.stations[2].num_docks_available);

    }

    @Test
    void merge() {

            //given
            String key = "66db3687-0aca-11e7-82f6-3863bb44ef7c";
            StationCache cache = new StationCache();
            StationFinder finder = new StationFinder(cache);

            //when
            Map<String, Station> stations = finder.merge();

            //then
            assertTrue(stations.containsKey(key));
            Station station = stations.get(key);
            assertNotEquals(0, station.lon);
            assertNotEquals(0, station.lat);
            assertNotEquals(0, station.num_docks_available);

    }

}
