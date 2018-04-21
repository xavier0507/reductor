package javatest.colorhaake.com.example3.reducer.main

import com.yheriatovych.reductor.Action
import com.yheriatovych.reductor.annotations.ActionCreator
import javatest.colorhaake.com.example3.model.ImageData
import javatest.colorhaake.com.example3.model.Response

@ActionCreator
interface MainActions {
    @ActionCreator.Action(VIEW_READY)
    fun viewReady(): Action

    @ActionCreator.Action(SEARCH_IMAGES)
    fun searchImages(term: String, page: Int): Action

    @ActionCreator.Action(SEARCH_IMAGES_RES)
    fun searchImagesRes(res: Response<ImageData>): Action

    companion object {
        const val VIEW_READY = "VIEW_READY"

        const val SEARCH_IMAGES = "SEARCH_IMAGES"
        const val SEARCH_IMAGES_RES = "SEARCH_IMAGES_RES"
    }
}
