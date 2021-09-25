package org.alberto97.aircontroller

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object CompatExtensions {

    fun Context.getCompatColor(@ColorRes color: Int): Int{
        return ContextCompat.getColor(this, color)
    }

    fun Context.getCompatDrawable(@DrawableRes drawable: Int): Drawable? {
        return ContextCompat.getDrawable(this, drawable)
    }

    fun ImageView.setTint(@ColorInt color: Int) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
    }

    fun View.setVisible(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: FragmentActivity> Fragment.requireAppActivity(): T {
        return this.requireActivity() as T
    }
}