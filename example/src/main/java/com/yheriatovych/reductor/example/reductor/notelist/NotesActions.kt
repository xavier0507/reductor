package com.yheriatovych.reductor.example.reductor.notelist

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.annotations.ActionCreator

@ActionCreator
interface NotesActions {

    @ActionCreator.Action(ADD_ACTION)
    fun add(id: Int, content: String): Action

    @ActionCreator.Action(REMOVE_ITEM)
    fun remove(id: Int): Action

    @ActionCreator.Action(TOGGLE)
    fun toggle(id: Int): Action

    companion object {
        const val ADD_ACTION = "ADD_ITEM"
        const val TOGGLE = "TOGGLE"
        const val REMOVE_ITEM = "REMOVE_ITEM"
    }
}
