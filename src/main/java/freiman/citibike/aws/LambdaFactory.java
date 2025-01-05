package freiman.citibike.aws;

import freiman.citibike.json.StationService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class LambdaFactory {

    public LambdaService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rlkpgpmpo7r2unoobpmfwav2eu0wrsut.lambda-url.us-east-2.on.aws/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(LambdaService.class);
    }
}
