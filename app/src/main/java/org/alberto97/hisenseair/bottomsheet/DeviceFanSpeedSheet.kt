package org.alberto97.hisenseair.bottomsheet

import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.features.fanToStringMap
import org.alberto97.hisenseair.models.BottomSheetListItem
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceFanSpeedSheet : BottomSheetListDialog<FanSpeed>() {

    val viewModel: DeviceViewModel by sharedViewModel()

    override val title: String = "Fan Speed"

    override fun getList(): List<BottomSheetListItem<FanSpeed>> {
        val list = listOf(
            FanSpeed.Lower,
            FanSpeed.Low,
            FanSpeed.Normal,
            FanSpeed.High,
            FanSpeed.Higher,
            FanSpeed.Auto,
        )

        return list.map { mapItem(it) }
    }

    private fun mapItem(data: FanSpeed): BottomSheetListItem<FanSpeed> {
        val text = getString(fanToStringMap.getValue(data))

        return BottomSheetListItem(
            data,
            text,
            R.drawable.ic_fan,
            data == viewModel.fanSpeed.value
        )
    }

    override fun onItemClick(data: FanSpeed) {
        viewModel.setFanSpeed(data)
        dismiss()
    }
}