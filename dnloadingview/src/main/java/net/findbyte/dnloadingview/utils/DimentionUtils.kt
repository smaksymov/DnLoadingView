package net.findbyte.dnloadingview.utils

import android.content.Context
import android.util.TypedValue

object DimentionUtils {
    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

}