package javatest.colorhaake.com.example3.injection.module;

import android.app.Application;
import android.content.Context;

import javatest.colorhaake.com.example3.injection.ApplicationContext;
import javatest.colorhaake.com.example3.model.AppState;
import javatest.colorhaake.com.example3.model.AppStateReducer;
import javatest.colorhaake.com.example3.reducer.main.MainReducer;
import com.yheriatovych.reductor.Store;
import com.yheriatovych.reductor.rxjava2.RxStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

// FIXME Has kapt compile issue when convert this file into kotlin
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application getApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    AppStateReducer provideReducer() {
        return AppStateReducer.builder()
                .addReducer(MainReducer.Companion.create())
                .build();
    }

    @Singleton
    @Provides
    Store<AppState> provideStore(AppStateReducer reducer) {
        return Store.create(
                reducer,
                AppState.Companion.initState()
        );
    }

    @Provides
    Observable<AppState> provideState(Store<AppState> store) {
        return RxStore.asObservable(store)
                // TODO try to move this
                .observeOn(AndroidSchedulers.mainThread());
    }
}
