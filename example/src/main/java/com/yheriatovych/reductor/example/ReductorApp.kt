package com.yheriatovych.reductor.example

import android.app.Application
import android.os.Handler
import android.util.Log
import com.facebook.stetho.Stetho
import com.facebook.stetho.inspector.console.RuntimeReplFactory
import com.facebook.stetho.rhino.JsRuntimeReplFactoryBuilder
import com.google.gson.GsonBuilder
import com.yheriatovych.reductor.Store
import com.yheriatovych.reductor.example.model.AppState
import com.yheriatovych.reductor.example.model.AppStateReducer
import com.yheriatovych.reductor.example.model.NotesFilter
import com.yheriatovych.reductor.example.reductor.filter.NotesFilterReducer
import com.yheriatovych.reductor.example.reductor.notelist.NotesListReducer
import com.yheriatovych.reductor.example.reductor.utils.SetStateReducer
import com.yheriatovych.reductor.example.reductor.utils.UndoableReducer
import org.mozilla.javascript.BaseFunction
import org.mozilla.javascript.Context
import org.mozilla.javascript.Function
import org.mozilla.javascript.Scriptable

class ReductorApp : Application() {

    lateinit var store: Store<AppState>
    private val gson = GsonBuilder()
            .registerTypeAdapterFactory(MyAdapterFactory.create())
            .create()

    private val handler = Handler()

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

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector {
                    Stetho.DefaultInspectorModulesBuilder(this@ReductorApp)
                            .runtimeRepl(createRuntimeRepl())
                            .finish()
                }
                .build())

    }

    private fun createRuntimeRepl(): RuntimeReplFactory {
        return JsRuntimeReplFactoryBuilder(this)
                .addFunction("getState", object : BaseFunction() {
                    override fun call(cx: Context?, scope: Scriptable?, thisObj: Scriptable?, args: Array<Any>?): Any {
                        val jsonString = gson.toJson(store.state)
                        val json = scope!!.get("JSON", scope) as Scriptable
                        val parseFunction = json.get("parse", scope) as Function
                        return parseFunction.call(cx, json, scope, arrayOf<Any>(jsonString))
                    }
                })
                .addFunction("setState", object : BaseFunction() {
                    override fun call(cx: Context?, scope: Scriptable?, thisObj: Scriptable?, args: Array<Any>?): Any {
                        val json = scope!!.get("JSON", scope) as Scriptable
                        val stringifyFunction = json.get("stringify", scope) as Function
                        val jsonString = stringifyFunction.call(cx, json, scope, arrayOf(args!![0])) as String

                        val arg = gson.fromJson(jsonString, AppState::class.java)
                        Log.d("ReductorApp", arg.toString())
                        handler.post { store.dispatch(SetStateReducer.setStateAction(arg)) }
                        return arg
                    }
                })
                .build()
    }
}
