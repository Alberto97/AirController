package org.alberto97.hisenseair.bottomsheet

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.ListAdapter
import org.alberto97.hisenseair.adapters.FanSpeedAdapter
import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.fragments.DEVICE_FRAGMENT_FAN_SHEET
import org.alberto97.hisenseair.models.FanSpeedItem

class DeviceFanSpeedSheet : ListBottomSheetDialog<FanSpeedItem, FanSpeed>() {
    override val title: String = "Fan Speed"

    override fun getList(): ArrayList<FanSpeedItem> {
        return arrayListOf(
            FanSpeedItem(FanSpeed.Lower, "Low"),
            FanSpeedItem(FanSpeed.Low, "Mid-Low"),
            FanSpeedItem(FanSpeed.Normal, "Normal"),
            FanSpeedItem(FanSpeed.High, "Mid-High"),
            FanSpeedItem(FanSpeed.Higher, "High"),
            FanSpeedItem(FanSpeed.Auto, "Auto")
        )
    }

    override fun getAdapter(): ListAdapter<FanSpeedItem, *> {
        return FanSpeedAdapter {
            val intent = Intent()
            intent.putExtra(DEVICE_FRAGMENT_FAN_SHEET, it.id)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

            dismiss()
        }
    }

    override fun getCurrentValue(): FanSpeed? {
        val c = arguments?.getInt("current") ?: return null
        return FanSpeed.from(c)
    }
}