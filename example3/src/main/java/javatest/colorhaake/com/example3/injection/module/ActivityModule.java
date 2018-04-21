package javatest.colorhaake.com.example3.injection.module;

import android.app.Activity;
import android.content.Context;

import javatest.colorhaake.com.example3.injection.ActivityContext;
import javatest.colorhaake.com.example3.model.AppState;
import javatest.colorhaake.com.example3.reducer.main.MainActions;
import javatest.colorhaake.com.example3.view.main.MainPresenter;
import com.yheriatovych.reductor.Actions;
import com.yheriatovych.reductor.Store;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() { return mActivity; }

    @Provides
    Activity provideActivity() { return mActivity; }

    @Provides
    MainActions provideMainActions() { return Actions.from(MainActions.class); }

    @Provides
    MainPresenter providePresenter(
            Observable<AppState> state, Store<AppState> store, MainActions actions
    ) {
        return new MainPresenter(state, store, actions);
    }
}
