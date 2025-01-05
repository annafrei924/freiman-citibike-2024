package freiman.citibike.json;

import freiman.citibike.aws.StationCache;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class StationServiceFactory {
    public StationService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gbfs.citibikenyc.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(StationService.class);
    }


    public Map<String, Station> merge(StationService service, StationCache cache) {
        System.out.println("merge is being called");
        Map<String, Station> stationsMap = new HashMap<>();
        Stations stationInfo = cache.getStations();
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