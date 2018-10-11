package javatest.colorhaake.com.example3.injection.component

import android.app.Application
import android.content.Context

import javatest.colorhaake.com.example3.Example3App
import javatest.colorhaake.com.example3.injection.ApplicationContext
import javatest.colorhaake.com.example3.injection.module.ApplicationModule
import javatest.colorhaake.com.example3.model.AppState
import com.yheriatovych.reductor.Store

import javax.inject.Singleton

import dagger.Component
import io.reactivex.Observable

@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    @get:ApplicationContext
    val context: Context

    val application: Application

    val store: Store<AppState>

    val state: Observable<AppState>
    fun inject(app: Example3App)
}
