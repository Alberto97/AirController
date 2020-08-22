package org.alberto97.hisenseair.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import org.alberto97.hisenseair.R

class TempControlPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    private lateinit var temp: TextView
    private lateinit var tempDown: ConstraintLayout
    private lateinit var tempUp: ConstraintLayout

    private var tempValue = 0
    private var tempClickListener: (v: View) -> Unit = { }
    private var downClickListener: (v: View) -> Unit = { }
    private var upClickListener: (v: View) -> Unit = { }

    init {
        layoutResource = R.layout.device_section_temperature
        isSelectable = false
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder?.isDividerAllowedAbove = true
        holder?.isDividerAllowedBelow = true

        temp = holder?.findViewById(R.id.temp) as TextView
        tempDown = holder.findViewById(R.id.tempDown) as ConstraintLayout
        tempUp = holder.findViewById(R.id.tempUp) as ConstraintLayout

        temp.text = context.getString(R.string.temp, tempValue)
        temp.setOnClickListener(tempClickListener)
        tempDown.setOnClickListener(downClickListener)
        tempUp.setOnClickListener(upClickListener)
    }

    fun setOnTempClickListener(listener: (v: View) -> Unit) {
        if (listener != tempClickListener) {
            tempClickListener = listener
            notifyChanged()
        }
    }

    fun setOnTempDownClickListener(listener: (v: View) -> Unit) {
        if (listener != downClickListener) {
            downClickListener = listener
            notifyChanged()
        }
    }

    fun setOnTempUpClickListener(listener: (v: View) -> Unit) {
        if (listener != upClickListener) {
            upClickListener = listener
            notifyChanged()
        }
    }

    fun setTemp(value: Int) {
        if (value != tempValue) {
            tempValue = value
            notifyChanged()
        }
    }
}