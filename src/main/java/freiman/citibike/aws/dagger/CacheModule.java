package freiman.citibike.aws.dagger;

import dagger.Module;
import dagger.Provides;
import freiman.citibike.aws.StationCache;

import javax.inject.Singleton;

@Module
public class CacheModule {
    @Provides
    @Singleton
    public static StationCache provideCache() {
        return new StationCache();
    }
}
