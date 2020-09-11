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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val speeds = viewModel.supportedFanSpeeds.value!!
        val list = speeds.map { mapItem(it) }

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    BottomSheetList(
                        title = "Fan Speed",
                        list = list,
                        onItemClick = { data -> onItemClick(data) }
                    )
                }
            }
        }
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

    private fun onItemClick(data: FanSpeed) {
        viewModel.setFanSpeed(data)
        dismiss()
    }
}