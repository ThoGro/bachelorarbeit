package edu.hm.ba.serverless.config;

import dagger.Component;
import edu.hm.ba.serverless.handler.*;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class,
        BookModule.class,
        StatisticModule.class
})
public interface AppComponent {

    void inject(CreateBookHandler requestHandler);

    void inject(GetBookHandler requestHandler);

    void inject(GetBooksHandler requestHandler);

    void inject(DeleteBookHandler requestHandler);

    void inject(UpdateBookHandler requestHandler);

    void inject(CreateStatisticHandler requestHandler);

    void inject(GetStatisticsHandler requestHandler);

    void inject(GetStatisticHandler requestHandler);

}
