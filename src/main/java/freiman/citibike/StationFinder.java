package freiman.citibike;

import freiman.citibike.json.Station;

import java.util.Map;

public class StationFinder {
    private Map<String, Station> stations;


    public StationFinder(Map<String, Station> stations) {
        this.stations = stations;
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
