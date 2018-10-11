package com.yheriatovych.reductor.example.model

import com.google.auto.value.AutoValue
import com.yheriatovych.reductor.annotations.CombinedState

@CombinedState
@AutoValue
abstract class AppState {

    val filteredNotes: List<Note>
        get() = notes().filter {
            filter() === NotesFilter.ALL
                    || filter() === NotesFilter.CHECKED && it.checked
                    || filter() === NotesFilter.UNCHECKED && it.checked.not()
        }

    abstract fun notes(): List<Note>

    abstract fun filter(): NotesFilter

    abstract fun toBuilder(): Builder
    fun withNotes(value: List<Note>): AppState {
        return toBuilder().setNotes(value).build()
    }

    fun withFilter(value: NotesFilter): AppState {
        return toBuilder().setFilter(value).build()
    }

    @AutoValue.Builder
    abstract class Builder {
        abstract fun setNotes(value: List<Note>): Builder
        abstract fun setFilter(value: NotesFilter): Builder
        abstract fun build(): AppState
    }

    companion object {
        fun builder(): Builder {
            return AutoValue_AppState.Builder()
        }
    }
}
