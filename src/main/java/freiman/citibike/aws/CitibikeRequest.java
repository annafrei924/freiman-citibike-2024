package freiman.citibike.aws;

public class CitibikeRequest {
    public Coordinate from;
    public Coordinate to;

    public CitibikeRequest(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }
}