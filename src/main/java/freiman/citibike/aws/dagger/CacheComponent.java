package freiman.citibike.aws.dagger;

import dagger.Component;
import freiman.citibike.aws.CitibikeRequestHandler;
import freiman.citibike.aws.StationCache;

import javax.inject.Singleton;

@Singleton
@Component(modules = {CacheModule.class})

public interface CacheComponent {

    CitibikeRequestHandler handler();

    StationCache cache();

    void inject(CitibikeRequestHandler handler);
}
