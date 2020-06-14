package org.alberto97.hisenseair.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.DevicePropertySwitchBinding

class ToggleMode : ConstraintLayout {
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    )
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    constructor(
//        context: Context,
//        attrs: AttributeSet?,
//        defStyleAttr: Int,
//        defStyleRes: Int)
//            : super(context, attrs, defStyleAttr, defStyleRes) {
//        init(context, attrs)
//    }

    private var _text = ""
    var text: String
        get() = _text
        set(value) = setTextInt(value)

    @DrawableRes
    private var _icon = R.drawable.ic_eco
    var icon: Int
        get() = _icon
        set(value) = setIconInt(value)

    private var _loading = false
    var loading: Boolean
        get() = _loading
        set(value) = setLoadingInt(value)

    lateinit var binding: DevicePropertySwitchBinding

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = DevicePropertySwitchBinding.inflate(LayoutInflater.from(context), this, true)
        binding.switch1.visibility = View.INVISIBLE

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToggleMode)
        val txt = typedArray.getText(R.styleable.ToggleMode_text)
        val icn = typedArray.getResourceId(R.styleable.ToggleMode_icon, R.drawable.ic_eco)

        text = txt.toString()
        icon = icn

        typedArray.recycle()
    }

    private fun setTextInt(text: String) {
        _text = text
        binding.text.text = text
    }

    private fun setIconInt(@DrawableRes drawable: Int) {
        _icon = drawable
        binding.icon.setImageResource(drawable)
    }

    private fun setLoadingInt(value: Boolean) {
        _loading = value
        binding.icon.visibility = if (value) View.GONE else View.VISIBLE
        binding.spinner.visibility = if (value) View.VISIBLE else View.GONE
    }
}