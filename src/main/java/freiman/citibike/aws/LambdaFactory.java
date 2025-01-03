package freiman.citibike.aws;

import freiman.citibike.json.StationService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class LambdaFactory {

    public static LambdaService service() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rlkpgpmpo7r2unoobpmfwav2eu0wrsut.lambda-url.us-east-2.on.aws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(LambdaService.class);
    }
}
