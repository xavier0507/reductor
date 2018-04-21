package javatest.colorhaake.com.example3.reducer.main;

import android.content.Context;

import javatest.colorhaake.com.example3.model.AppState;
import com.yheriatovych.reductor.Actions;
import com.yheriatovych.reductor.Commands;
import com.yheriatovych.reductor.Pair;
import com.yheriatovych.reductor.Reducer;
import com.yheriatovych.reductor.annotations.AutoReducer;

@AutoReducer
public abstract class MainReducer implements Reducer<AppState> {

    private MainActions mainActions = Actions.from(MainActions.class);

    @AutoReducer.Action(
            value = MainActions.VIEW_READY,
            from = MainActions.class
    )
    public Pair<AppState, Commands<AppState>> viewReady(AppState state, Context context) {
        return Pair.create(state);
    }

    @AutoReducer.Action(
            value = MainActions.SEARCH_IMAGES_RES,
            from = MainActions.class
    )
    public Pair<AppState, Commands<AppState>> searchImagesRes(AppState state) {
        return Pair.create(state);
    }

    public static MainReducer create() {
        return new MainReducerImpl();
    }
}
