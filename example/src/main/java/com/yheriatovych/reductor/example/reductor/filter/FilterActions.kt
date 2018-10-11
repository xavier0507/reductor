package com.yheriatovych.reductor.example.reductor.filter

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.annotations.ActionCreator
import com.yheriatovych.reductor.example.model.NotesFilter

@ActionCreator
interface FilterActions {

    @ActionCreator.Action(SET_FILTER)
    fun setFilter(filter: NotesFilter): Action

    companion object {
        const val SET_FILTER = "SET_FILTER"
    }
}
