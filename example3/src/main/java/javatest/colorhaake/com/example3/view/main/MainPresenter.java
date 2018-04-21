package javatest.colorhaake.com.example3.view.main;

import android.app.Activity;

import javatest.colorhaake.com.example3.model.AppState;
import javatest.colorhaake.com.example3.reducer.main.MainActions;
import javatest.colorhaake.com.example3.view.base.BasePresenter;
import com.yheriatovych.reductor.Store;

import java.util.ArrayList;

import io.reactivex.Observable;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private final Observable<AppState> state;
    private final Store<AppState> store;
    private final MainActions actions;

    public MainPresenter(
            Observable<AppState> state,
            Store<AppState> store,
            MainActions actions
    ) {

        this.state = state;
        this.store = store;
        this.actions = actions;

        this.state.subscribe(newState ->
                getMvpView().showMainPage(new ArrayList<>())
        );
    }

    public void viewReady(Activity activity) {
        store.dispatch(actions.viewReady(activity));
    }
}
