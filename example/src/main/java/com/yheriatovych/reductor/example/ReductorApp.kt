package com.yheriatovych.reductor.example

import android.app.Application
import com.yheriatovych.reductor.Store
import com.yheriatovych.reductor.example.model.AppState
import com.yheriatovych.reductor.example.model.AppStateReducer
import com.yheriatovych.reductor.example.model.NotesFilter
import com.yheriatovych.reductor.example.reductor.filter.NotesFilterReducer
import com.yheriatovych.reductor.example.reductor.notelist.NotesListReducer
import com.yheriatovych.reductor.example.reductor.utils.SetStateReducer
import com.yheriatovych.reductor.example.reductor.utils.UndoableReducer

class ReductorApp : Application() {

    lateinit var store: Store<AppState>

    override fun onCreate() {
        super.onCreate()

        val vanillaReducer = AppStateReducer.builder()
                .addReducer(NotesListReducer.create())
                .addReducer(NotesFilterReducer.create())
                .build()

        store = Store.create(
                SetStateReducer(UndoableReducer(vanillaReducer)),
                AppState.builder().setFilter(NotesFilter.ALL).setNotes(listOf()).build()
        )
    }
}
