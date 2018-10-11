package javatest.colorhaake.com.example3.injection.module

import android.app.Activity
import android.content.Context

import javatest.colorhaake.com.example3.injection.ActivityContext
import javatest.colorhaake.com.example3.model.AppState
import javatest.colorhaake.com.example3.reducer.main.MainActions
import javatest.colorhaake.com.example3.view.main.MainPresenter
import com.yheriatovych.reductor.Actions
import com.yheriatovych.reductor.Store

import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class ActivityModule(private val mActivity: Activity) {

    @Provides
    @ActivityContext
    fun provideContext(): Context {
        return mActivity
    }

    @Provides
    fun provideActivity(): Activity {
        return mActivity
    }

    @Provides
    fun provideMainActions(): MainActions {
        return Actions.from(MainActions::class.java)
    }

    @Provides
    fun providePresenter(
            state: Observable<AppState>, store: Store<AppState>, actions: MainActions
    ): MainPresenter {
        return MainPresenter(state, store, actions)
    }
}
