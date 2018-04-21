package javatest.colorhaake.com.example3.view.main

import javatest.colorhaake.com.example3.view.base.MvpView

interface MainMvpView : MvpView {
    fun showMainPage(list: List<String>)
}
