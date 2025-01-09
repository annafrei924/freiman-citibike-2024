package freiman.citibike;

import freiman.citibike.aws.StationCache;
import freiman.citibike.json.Station;
import freiman.citibike.json.Stations;

import java.util.HashMap;
import java.util.Map;

public class StationFinder {
    private StationCache cache;
    private Map<String, Station> stations;

    public StationFinder(StationCache cache) {
        this.cache = cache;
        stations = merge();
    }

    public Map<String, Station> merge() {
        Map<String, Station> stationsMap = new HashMap<>();
        Stations stationInfo = cache.getStations();
        Stations stationStatus = cache.getService().stationStatus().blockingGet();
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

    public Station closestStation(double lat, double lon, boolean returning) {
        double minDistance = Double.POSITIVE_INFINITY;
        Station closestStation = null;

        for (Station station : stations.values()) {
            double currDistance = Math.sqrt((lat - station.lat) * (lat - station.lat)) +
                    ((lon - station.lon) * (lon - station.lon));

            if (returning && currDistance < minDistance && station.num_docks_available > 0) {
                minDistance = currDistance;
                closestStation = station;
            } else if (!returning && currDistance < minDistance && station.num_bikes_available > 0) {
                minDistance = currDistance;
                closestStation = station;
            }
        }

        return closestStation;
    }

}
