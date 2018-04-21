package javatest.colorhaake.com.example3.reducer.main;

import android.content.Context;

import com.yheriatovych.reductor.Action;
import com.yheriatovych.reductor.annotations.ActionCreator;

@ActionCreator
public interface MainActions {
    String VIEW_READY = "VIEW_READY";
    @ActionCreator.Action(VIEW_READY)
    Action viewReady(Context context);

    String SEARCH_IMAGES_RES = "SEARCH_IMAGES_RES";
    @ActionCreator.Action(SEARCH_IMAGES_RES)
    Action searchImagesRes();
}
