package com.yheriatovych.reductor.example.reductor.filter

import com.yheriatovych.reductor.Commands
import com.yheriatovych.reductor.Pair
import com.yheriatovych.reductor.Reducer
import com.yheriatovych.reductor.annotations.AutoReducer
import com.yheriatovych.reductor.annotations.AutoReducer.Action
import com.yheriatovych.reductor.example.model.AppState
import com.yheriatovych.reductor.example.model.NotesFilter

@AutoReducer
abstract class NotesFilterReducer : Reducer<AppState> {
    @Action(value = FilterActions.SET_FILTER, from = FilterActions::class)
    fun setFilter(state: AppState, value: NotesFilter): Pair<AppState, Commands<AppState>> {
        return Pair.create(state.withFilter(value))
    }

    companion object {
        fun create(): NotesFilterReducer {
            return NotesFilterReducerImpl()
        }
    }
}
