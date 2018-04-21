package javatest.colorhaake.com.example3;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDex;

import javatest.colorhaake.com.example3.injection.component.ApplicationComponent;
import javatest.colorhaake.com.example3.injection.component.DaggerApplicationComponent;
import javatest.colorhaake.com.example3.injection.module.ApplicationModule;
import javatest.colorhaake.com.example3.model.AppState;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.yheriatovych.reductor.Store;

import javax.inject.Inject;

public class Example3App extends Application {
    public final String TAG = Example3App.class.getName();

    protected ApplicationComponent applicationComponent;

    @Inject public Store<AppState> store;

    public static Example3App get(Activity activity) {
        return (Example3App) activity.getApplication();
    }

    public ApplicationComponent component() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);

        // for using Fresco Image
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
