package freiman.citibike.aws.dagger;

import dagger.Component;
import freiman.citibike.aws.StationCache;
import javax.inject.Singleton;

@Singleton
@Component(modules = {CacheModule.class})

public interface CacheComponent {
    StationCache cache();
}
