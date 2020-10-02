package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.ui.BottomSheetList
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceWorkModeSheet : ComposeBottomSheetDialogFragment() {

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
                        title = "Mode",
                        list = viewModel.getSupportedModes(),
                        onItemClick = { data -> onItemClick(data) }
                    )
                }
            }
        }
    }

    private fun onItemClick(data: WorkMode) {
        viewModel.setMode(data)
        dismiss()
    }
}