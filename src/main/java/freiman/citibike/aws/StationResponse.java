package freiman.citibike.aws;

public class StationResponse {
    public double lat;
    public double lon;
    public String name;
    public String station_id;

    public StationResponse(double lat, double lon, String name, String station_id) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.station_id = station_id;
    }
}