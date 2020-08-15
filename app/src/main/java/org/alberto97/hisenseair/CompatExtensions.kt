package org.alberto97.hisenseair

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

object CompatExtensions {

    fun Context.getCompatColor(@ColorRes color: Int): Int{
        return ContextCompat.getColor(this, color)
    }

    fun ImageView.setTint(@ColorInt color: Int) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
    }

    fun View.setVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }
}