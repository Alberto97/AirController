package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.models.BottomSheetListItem
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

        val modes = viewModel.supportedModes.value!!
        val list = modes.map { mapItem(it) }

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    BottomSheetList(
                        title = "Mode",
                        list = list,
                        onItemClick = { data -> onItemClick(data) }
                    )
                }
            }
        }
    }

    private fun mapItem(data: WorkMode): BottomSheetListItem<WorkMode> {
        val text = getString(modeToStringMap.getValue(data))
        val icon = modeToIconMap.getValue(data)

        return BottomSheetListItem(
            data,
            text,
            icon,
            data == viewModel.workState.value
        )
    }

    private fun onItemClick(data: WorkMode) {
        viewModel.setMode(data)
        dismiss()
    }
}