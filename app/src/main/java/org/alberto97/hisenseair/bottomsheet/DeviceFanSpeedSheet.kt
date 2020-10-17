package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.hisenseair.features.FanSpeed
import org.alberto97.hisenseair.ui.BottomSheetList
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceFanSpeedSheet : BottomSheetDialogFragment() {

    val viewModel: DeviceViewModel by sharedViewModel()

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
                        list = viewModel.getSupportedFanSpeed(),
                        onItemClick = { data -> onItemClick(data) }
                    )
                }
            }
        }
    }

    private fun onItemClick(data: FanSpeed) {
        viewModel.setFanSpeed(data)
        findNavController().popBackStack()
    }
}