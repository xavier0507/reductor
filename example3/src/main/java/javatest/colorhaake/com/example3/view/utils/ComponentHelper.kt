package javatest.colorhaake.com.example3.view.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

object ComponentHelper {

    /**
     * @param context application/activity context
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param map HashMap<Int, (TypedArray, Int) -> Unit>
     *  <Int> the attr resource id that you want to find in attrs,
     *  <(TypedArray, Int) -> Unit>, A callback function after finding specific attr resource id
     *  <TypedArray> contains real attribute values, <Int> is index in <TypedArray>
     */
    fun setupAttrs(
            context: Context, attrs: AttributeSet, map: Map<Int, (TypedArray, Int) -> Unit>
    ) {
        // for android limitation, you need to sort all attrs you want to find in ascending order
        // then find attrs, otherwise some attrs cannot be found
        // https://developer.android.com/reference/android/content/res/Resources.Theme.html#obtainStyledAttributes(android.util.AttributeSet, int[], int, int)
        val sorted = map.toSortedMap()
        val set = sorted.keys.toIntArray().apply { sort() }

        // extract out attribute from your layout xml
        val a = context.obtainStyledAttributes(attrs, set)

        (0 until a.indexCount)
                .map(a::getIndex)
                .forEach {
                    // map to what you want to do
                    sorted[set[it]]?.invoke(a, it)
                }

        a.recycle()
    }
}
