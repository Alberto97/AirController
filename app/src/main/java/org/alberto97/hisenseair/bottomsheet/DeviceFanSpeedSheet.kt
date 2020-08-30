package org.alberto97.hisenseair.bottomsheet

import androidx.recyclerview.widget.ListAdapter
import org.alberto97.hisenseair.adapters.FanSpeedAdapter
import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.models.FanSpeedItem
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceFanSpeedSheet : ListBottomSheetDialog<FanSpeedItem, FanSpeed>() {

    val viewModel: DeviceViewModel by sharedViewModel()

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

    override fun getAdapter(): ListAdapter<FanSpeedItem, *> = FanSpeedAdapter { onItemClick(it) }

    private fun onItemClick(data: FanSpeedItem) {
        viewModel.setFanSpeed(data.id)
        dismiss()
    }

    override fun getCurrentValue() = viewModel.fanSpeed.value
}