package freiman.citibike.json;

public class Station {
    //station info
    //CHECKSTYLE:OFF
    public String station_id;
    //CHECKSTYLE:ON
    public String name;
    public double lon;
    public double lat;

    //station status
    public int num_docks_available;
    public int num_bikes_available;


}
