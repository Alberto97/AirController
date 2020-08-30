package org.alberto97.hisenseair.bottomsheet

import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.models.BottomSheetListItem
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceWorkModeSheet : BottomSheetListDialog<WorkMode>() {

    val viewModel: DeviceViewModel by sharedViewModel()

    override val title = "Mode"

    override fun getList(): List<BottomSheetListItem<WorkMode>> {
        val list = listOf(
            WorkMode.Heating,
            WorkMode.Cooling,
            WorkMode.Dry,
            WorkMode.FanOnly,
            WorkMode.Auto
        )

        return list.map { mapItem(it) }
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

    override fun onItemClick(data: WorkMode) {
        viewModel.setMode(data)
        dismiss()
    }
}