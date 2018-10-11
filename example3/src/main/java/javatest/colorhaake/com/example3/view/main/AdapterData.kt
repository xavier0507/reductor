package javatest.colorhaake.com.example3.view.main

object ViewType {
    const val SEARCH_BAR = 0
    const val SEARCH_COUNT = 1
    const val SEARCH_IMAGES = 2
}

abstract class Item<out T>(val mData: T?) {
    abstract fun getViewType(): Int
}

