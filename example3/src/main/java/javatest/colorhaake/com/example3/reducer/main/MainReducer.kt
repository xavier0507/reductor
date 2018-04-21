package javatest.colorhaake.com.example3.reducer.main

import android.content.Context

import javatest.colorhaake.com.example3.model.AppState
import com.yheriatovych.reductor.Actions
import com.yheriatovych.reductor.Commands
import com.yheriatovych.reductor.Pair
import com.yheriatovych.reductor.Reducer
import com.yheriatovych.reductor.annotations.AutoReducer

@AutoReducer
abstract class MainReducer : Reducer<AppState> {

    private val mainActions = Actions.from(MainActions::class.java)

    @AutoReducer.Action(value = MainActions.VIEW_READY, from = MainActions::class)
    fun viewReady(state: AppState, context: Context): Pair<AppState, Commands<AppState>> {
        return Pair.create(state)
    }

    @AutoReducer.Action(value = MainActions.SEARCH_IMAGES_RES, from = MainActions::class)
    fun searchImagesRes(state: AppState): Pair<AppState, Commands<AppState>> {
        return Pair.create(state)
    }

    companion object {
        fun create(): MainReducer {
            return MainReducerImpl()
        }
    }
}
