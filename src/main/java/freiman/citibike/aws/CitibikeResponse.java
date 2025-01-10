package freiman.citibike.aws;

import freiman.citibike.json.Station;

public class CitibikeResponse {
    public Coordinate from;
    public Coordinate to;
    public Station start;
    public Station end;

    public CitibikeResponse(Coordinate from, Coordinate to, Station start, Station end) {
        this.from = from;
        this.to = to;
        this.start = start;
        this.end = end;
    }
}