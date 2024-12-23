package freiman.citibike.json;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class StationServiceFactory {

    public StationService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gbfs.citibikenyc.com/")
                // Configure Retrofit to use Gson to turn the Json into Objects
                .addConverterFactory(GsonConverterFactory.create())
                // Configure Retrofit to use Rx
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(StationService.class);
    }

    public Map<String, Station> merge(StationService service) {
        Map<String, Station> stationsMap = new HashMap<>();
        Stations stationInfo = service.stationInfo().blockingGet();
        Stations stationStatus = service.stationStatus().blockingGet();
        //First, add all station info (name, lon, lat) into the map
        for (Station station : stationInfo.data.stations) {
            stationsMap.put(station.station_id, station);
        }
        for (Station station : stationStatus.data.stations) {
            Station existingStation = stationsMap.get(station.station_id);
            if (existingStation != null) {
                existingStation.num_docks_available = station.num_docks_available;
                existingStation.num_bikes_available = station.num_bikes_available;
            }
        }
        return stationsMap;
    }
}
