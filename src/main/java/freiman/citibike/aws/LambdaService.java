package freiman.citibike.aws;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LambdaService {
    @POST("/")
    Single<CitibikeResponse> getLambda(@Body CitibikeRequest request);
}