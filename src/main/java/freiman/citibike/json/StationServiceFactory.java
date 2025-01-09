package freiman.citibike.json;

import freiman.citibike.aws.StationCache;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class StationServiceFactory {
    public StationService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gbfs.citibikenyc.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(StationService.class);
    }
}