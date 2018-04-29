package javatest.colorhaake.com.example3.reducer.main

import javatest.colorhaake.com.example3.model.AppState
import com.yheriatovych.reductor.Actions
import com.yheriatovych.reductor.Commands
import com.yheriatovych.reductor.Pair
import com.yheriatovych.reductor.Reducer
import com.yheriatovych.reductor.annotations.AutoReducer
import com.yheriatovych.reductor.observable.EpicCommands
import javatest.colorhaake.com.example3.model.ImageData
import javatest.colorhaake.com.example3.model.Response
import javatest.colorhaake.com.example3.network.NetworkApi

@AutoReducer
abstract class MainReducer : Reducer<AppState> {

    private val mainActions = Actions.from(MainActions::class.java)

    @AutoReducer.Action(value = MainActions.VIEW_READY, from = MainActions::class)
    fun viewReady(state: AppState): Pair<AppState, Commands<AppState>> {
        return Pair.create(state)
    }

    @AutoReducer.Action(value = MainActions.SEARCH_IMAGES, from = MainActions::class)
    fun searchImages(
            state: AppState, term: String, page: Int = 1
    ): Pair<AppState, Commands<AppState>> {
        if (term.isEmpty() || page < 1) return Pair.create(state)

        return Pair.create(
                state,
                EpicCommands.create(
                        NetworkApi.searchData(term, page).map(mainActions::searchImagesRes)
                )
        )
    }

    @AutoReducer.Action(value = MainActions.SEARCH_IMAGES_RES, from = MainActions::class)
    fun searchImagesRes(state: AppState, res: Response<ImageData>): Pair<AppState, Commands<AppState>> {
        if (res.metadata.isNotSuccess) {
            return Pair.create(state)
        }

        return Pair.create(state.withSearchImageData(res))
    }

    companion object {
        fun create(): MainReducer {
            return MainReducerImpl()
        }
    }
}
