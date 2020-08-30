package org.alberto97.hisenseair.bottomsheet

import androidx.annotation.ColorRes
import androidx.recyclerview.widget.ListAdapter
import org.alberto97.hisenseair.CompatExtensions.getCompatColor
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.adapters.WorkModeAdapter
import org.alberto97.hisenseair.features.WorkMode
import org.alberto97.hisenseair.models.WorkModeItem
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceWorkModeSheet : ListBottomSheetDialog<WorkModeItem, WorkMode>() {

    val viewModel: DeviceViewModel by sharedViewModel()

    override val title = "Mode"

    override fun getList(): ArrayList<WorkModeItem> {
        return arrayListOf(
            WorkModeItem(
                WorkMode.Heating,
                R.drawable.round_brightness_7_24,
                "Heating",
                getColor(R.color.material_orange_500)
            ),
            WorkModeItem(
                WorkMode.Cooling,
                R.drawable.ic_weather_windy,
                "Cooling",
                getColor(R.color.material_blue_500)
            ),
            WorkModeItem(WorkMode.Dry,
                R.drawable.water_outline,
                "Dry",
                getColor(R.color.material_blue_500)
            ),
            WorkModeItem(
                WorkMode.FanOnly,
                R.drawable.ic_fan,
                "Fan Only",
                getColor(R.color.material_cyan_500)
            ),
            WorkModeItem(WorkMode.Auto,
                R.drawable.ic_air_conditioner,
                "Auto",
                getColor(R.color.material_cyan_500)
            )
        )
    }

    override fun getAdapter(): ListAdapter<WorkModeItem, *> = WorkModeAdapter { onItemClick(it) }

    private fun onItemClick(data: WorkModeItem) {
        viewModel.setMode(data.id)
        dismiss()
    }

    override fun getCurrentValue() = viewModel.workState.value

    private fun getColor(@ColorRes color: Int) = requireContext().getCompatColor(color)
}