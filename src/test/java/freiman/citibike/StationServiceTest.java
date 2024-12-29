package freiman.citibike;

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
        assertNotNull(collection.data.stations[0].lon);
        assertNotNull(collection.data.stations[0].lat);

    }


    @Test
    void stationStatus() {
        // Given
        StationService service = new StationServiceFactory().getService();

        // When
        Stations collection = service.stationStatus().blockingGet();

        //then
        assertNotNull(collection.data.stations[2].station_id);
        assertNotNull(collection.data.stations[0].num_bikes_available);
        assertNotNull(collection.data.stations[0].num_docks_available);
    }

    @Test
    void merge() {
        //given
        StationServiceFactory factory = new StationServiceFactory();
        StationService service = factory.getService();
        String key = "66db3687-0aca-11e7-82f6-3863bb44ef7c";

        //when
        Map<String, Station> stations = factory.merge(service);

        //then
        assertNotNull(stations.containsKey(key));
        Station station = stations.get(key);
        assertNotNull(station.lon);
        assertNotEquals(0, station.lon);
        assertNotNull(station.lat);
        assertNotEquals(0, station.lat);
        assertNotNull(station.num_docks_available);
        assertNotNull(station.num_bikes_available);
    }

}
