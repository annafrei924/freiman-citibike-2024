package freiman.citibike.json;

import freiman.citibike.aws.CitibikeRequest;
import freiman.citibike.aws.CitibikeResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface StationService {
    @GET("/gbfs/en/station_information.json")
    Single<Stations> stationInfo();

    @GET("/gbfs/en/station_status.json")
    Single<Stations> stationStatus();
}
