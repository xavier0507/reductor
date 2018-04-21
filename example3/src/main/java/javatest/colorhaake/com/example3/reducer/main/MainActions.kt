package javatest.colorhaake.com.example3.reducer.main

import android.content.Context

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.annotations.ActionCreator

@ActionCreator
interface MainActions {
    @ActionCreator.Action(VIEW_READY)
    fun viewReady(context: Context): Action

    @ActionCreator.Action(SEARCH_IMAGES_RES)
    fun searchImagesRes(): Action

    companion object {
        const val VIEW_READY = "VIEW_READY"

        const val SEARCH_IMAGES_RES = "SEARCH_IMAGES_RES"
    }
}
