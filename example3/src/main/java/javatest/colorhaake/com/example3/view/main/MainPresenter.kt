package javatest.colorhaake.com.example3.view.main

import android.app.Activity

import javatest.colorhaake.com.example3.model.AppState
import javatest.colorhaake.com.example3.reducer.main.MainActions
import javatest.colorhaake.com.example3.view.base.BasePresenter
import com.yheriatovych.reductor.Store

import java.util.ArrayList

import io.reactivex.Observable

class MainPresenter(
        private val state: Observable<AppState>,
        private val store: Store<AppState>,
        private val actions: MainActions
) : BasePresenter<MainMvpView>() {

    init {
        state.subscribe { mvpView?.showMainPage(ArrayList()) }
    }

    fun viewReady(activity: Activity) {
        store.dispatch(actions.viewReady(activity))
    }
}
