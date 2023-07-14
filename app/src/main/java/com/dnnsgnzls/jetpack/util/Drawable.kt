package com.dnnsgnzls.jetpack.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.dnnsgnzls.jetpack.R

fun getProgressDrawable(
    context: Context,
    strokeWidth: Float = 10f,
    centerRadius: Float = 50f,
    colorResId: Int = R.color.color_accent
): CircularProgressDrawable {
    val color = ContextCompat.getColor(context, colorResId)
    return CircularProgressDrawable(context).apply {
        this.strokeWidth = strokeWidth
        this.centerRadius = centerRadius
        setColorSchemeColors(color)
        start()
    }
}
