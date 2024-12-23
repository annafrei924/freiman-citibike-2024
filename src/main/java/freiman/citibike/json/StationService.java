package freiman.citibike.json;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface StationService {
    @GET("/gbfs/en/station_information.json")
    Single<Stations> stationInfo();

    @GET("/gbfs/en/station_status.json")
    Single<Stations> stationStatus();


}
