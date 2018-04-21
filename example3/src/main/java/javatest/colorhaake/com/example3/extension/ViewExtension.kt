package javatest.colorhaake.com.example3.extension

import android.view.View
import android.view.ViewGroup

fun View.setSize(width: Int, height: Int) {
    val newLayoutParams = layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    )

    newLayoutParams.width = width
    newLayoutParams.height = height
    layoutParams = newLayoutParams
}