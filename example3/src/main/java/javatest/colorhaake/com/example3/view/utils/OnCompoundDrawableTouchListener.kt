package javatest.colorhaake.com.example3.view.utils

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

interface OnCompoundDrawableTouchListener : View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v == null || event == null || v !is TextView) return false

        val x = event.x.toInt()
        val y = event.y.toInt()

        val textView: TextView = v
        val drawables = textView.compoundDrawables

        val onTouchIndexedDrawable = drawables.withIndex()
                .filter { it.value != null }
                .map { IndexedValue(it.index, getDrawableBounds(it.index, it.value, textView)) }
                .firstOrNull { it.value.contains(x, y) }
                ?: return false

        with (onTouchIndexedDrawable) {
            val relativeEvent = MotionEvent.obtain(
                    event.downTime,
                    event.eventTime,
                    event.action,
                    event.x - value.left,
                    event.y - value.top,
                    event.metaState
            )
            return onDrawableTouch(v, index, value, relativeEvent)
        }
    }

    private fun getDrawableBounds(index: Int, drawable: Drawable, view: TextView): Rect {
        val drawableLeftWidth = view.compoundDrawables[LEFT]
                ?.takeIf(Drawable::isVisible)?.intrinsicWidth ?: 0
        val drawableRightWidth = view.compoundDrawables[RIGHT]
                ?.takeIf(Drawable::isVisible)?.intrinsicWidth ?: 0
        val horizontalDrawableOffset = drawableLeftWidth - drawableRightWidth
        return when (index) {
            LEFT -> Rect(
                    view.paddingLeft,
                    view.paddingTop,
                    view.paddingLeft + drawable.intrinsicWidth,
                    view.paddingTop + drawable.intrinsicHeight
            )
            TOP -> Rect(
                    horizontalDrawableOffset + view.width / 2 - drawable.intrinsicWidth / 2,
                    view.paddingTop,
                    horizontalDrawableOffset + view.width / 2 + drawable.intrinsicWidth / 2,
                    view.paddingTop + drawable.intrinsicHeight
            )
            RIGHT -> Rect(
                    view.width - view.paddingRight - drawable.intrinsicWidth,
                    view.paddingTop,
                    view.width - view.paddingRight,
                    view.paddingTop + drawable.intrinsicHeight
            )
            BOTTOM -> Rect(
                    horizontalDrawableOffset + view.width / 2 - drawable.intrinsicWidth / 2,
                    view.height - view.paddingBottom - drawable.intrinsicHeight,
                    horizontalDrawableOffset + view.width / 2 + drawable.intrinsicWidth / 2,
                    view.height - view.paddingBottom
            )
            else -> Rect()
        }
    }

    fun onDrawableTouch(v: TextView, drawableIndex: Int, drawableBounds: Rect, event: MotionEvent): Boolean

    companion object {
        const val LEFT: Int = 0
        const val TOP: Int = 1
        const val RIGHT: Int = 2
        const val BOTTOM: Int = 3
    }
}