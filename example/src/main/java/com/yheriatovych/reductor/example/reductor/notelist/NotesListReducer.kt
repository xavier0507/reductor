package com.yheriatovych.reductor.example.reductor.notelist

import com.yheriatovych.reductor.Commands
import com.yheriatovych.reductor.Pair
import com.yheriatovych.reductor.Reducer
import com.yheriatovych.reductor.annotations.AutoReducer
import com.yheriatovych.reductor.annotations.AutoReducer.Action
import com.yheriatovych.reductor.example.model.AppState
import com.yheriatovych.reductor.example.model.Note

@AutoReducer
abstract class NotesListReducer : Reducer<AppState> {

    @Action(value = NotesActions.ADD_ACTION, from = NotesActions::class)
    fun add(state: AppState, id: Int, content: String): Pair<AppState, Commands<AppState>> {
        val notes = state.notes() + Note(id, content, false)
        return Pair.create(state.withNotes(notes))
    }

    @Action(value = NotesActions.TOGGLE, from = NotesActions::class)
    fun toggle(state: AppState, noteId: Int): Pair<AppState, Commands<AppState>> {
        val notes = state.notes().map {
            if (it.id == noteId) it.copy(checked = it.checked.not())
            else it
        }
        return Pair.create(state.withNotes(notes))
    }

    @Action(value = NotesActions.REMOVE_ITEM, from = NotesActions::class)
    fun remove(state: AppState, id: Int): Pair<AppState, Commands<AppState>> {
        val notes = state.notes().filter { it.id != id }
        return Pair.create(state.withNotes(notes))
    }

    companion object {
        fun create(): NotesListReducer {
            return NotesListReducerImpl()
        }
    }
}
