package org.alberto97.hisenseair.ui

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.ButtonToggleBinding


class ToggleButton : LinearLayout {
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private lateinit var binding: ButtonToggleBinding
    private var lightTheme = false

    private var _text = ""
    var text: String
        get() = _text
        set(value) = setTextInt(value)

    @DrawableRes
    private var _icon = R.drawable.ic_eco
    var icon: Int
        get() = _icon
        set(value) = setIconInt(value)

    private var _active = false
    var active: Boolean
        get() = _active
        set(value) { setActiveInt(value) }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = ButtonToggleBinding.inflate(LayoutInflater.from(context), this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToggleButton)
        val txt = typedArray.getText(R.styleable.ToggleButton_text)
        val icn = typedArray.getResourceId(R.styleable.ToggleButton_icon, R.drawable.ic_eco)
        lightTheme = typedArray.getBoolean(R.styleable.ToggleButton_lightMode, false)

        text = txt.toString()
        icon = icn
        active = false

        typedArray.recycle()
    }

    private fun setActiveInt(active: Boolean) {
        _active = active

        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorControlNormal, typedValue, true)

        val iconColor = if (lightTheme) { Color.WHITE } else { /*getColor(R.color.materialGray600)*/ ContextCompat.getColor(context, typedValue.resourceId) }
        val iconActiveColor = if (lightTheme) { getColor(R.color.primary) } else { Color.WHITE }
        val strokeColor = if (lightTheme) { Color.parseColor("#77ffffff") } else { Color.parseColor("#e5e5e5") }
        val backgroundColor = if (lightTheme) { Color.WHITE } else { getColor(R.color.primary) }

        binding.text.setTextColor(iconColor)

        val color =  if (active) { iconActiveColor } else { iconColor }
        val background = getToggleBackground()
        if (active) {
            background.setColor(backgroundColor)
        } else {
            background.setStroke(2, strokeColor)
        }

        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled)
        )
        binding.icon.imageTintList = ColorStateList(states, intArrayOf(color))
        binding.iconParent.background = background
    }

    private fun getToggleBackground(): GradientDrawable {
        val dimens = context.resources.getDimension(R.dimen.toggleCircleSize).toInt()
        val background = GradientDrawable()
        background.apply {
            shape = GradientDrawable.OVAL
            setSize(dimens, dimens)
        }

        return background
    }

    private fun setIconInt(@DrawableRes drawable: Int) {
        _icon = drawable
        binding.icon.setImageResource(drawable)
    }

    private fun setTextInt(text: String) {
        _text = text
        binding.text.text = text
        binding.text.visibility = if (text.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun getColor(@ColorRes value: Int): Int {
        return ContextCompat.getColor(context, value)
    }

}