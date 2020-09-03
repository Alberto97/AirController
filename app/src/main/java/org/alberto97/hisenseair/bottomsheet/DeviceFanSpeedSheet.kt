package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.features.fanToStringMap
import org.alberto97.hisenseair.models.BottomSheetListItem
import org.alberto97.hisenseair.ui.BottomSheetList
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceFanSpeedSheet : ComposeBottomSheetDialogFragment() {

    val viewModel: DeviceViewModel by sharedViewModel()

    private fun getList(): List<BottomSheetListItem<FanSpeed>> {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    BottomSheetList(
                        title = "Fan Speed",
                        list = getList(),
                        onItemClick = { data -> onItemClick(data) }
                    )
                }
            }
        }
    }

    private fun onItemClick(data: FanSpeed) {
        viewModel.setFanSpeed(data)
        dismiss()
    }
}