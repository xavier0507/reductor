package javatest.colorhaake.com.example3.view.main

import javatest.colorhaake.com.example3.model.ImageData
import javatest.colorhaake.com.example3.model.Response
import javatest.colorhaake.com.example3.view.base.MvpView

interface MainMvpView : MvpView {
    fun showMainPage(searchImageData: Response<ImageData>)
}
