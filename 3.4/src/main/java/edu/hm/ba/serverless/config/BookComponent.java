package edu.hm.ba.serverless.config;

import dagger.Component;
import edu.hm.ba.serverless.dao.BookDao;
import edu.hm.ba.serverless.handler.CreateBookHandler;
import edu.hm.ba.serverless.handler.GetBookHandler;

import javax.inject.Singleton;

@Singleton
@Component(modules = {BookModule.class})
public interface BookComponent {

    //BookDao provideBookDao();

    void inject(CreateBookHandler requestHandler);

    void inject(GetBookHandler requestHandler);

}
