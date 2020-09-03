package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.hisenseair.ui.devicecontrol.TemperatureControl
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TemperatureControlSheet :  BottomSheetDialogFragment() {

    private val viewModel: DeviceViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    TemperatureControl(
                        temp = viewModel.temp.value!!.toFloat(),
                        min = viewModel.minTemp.value!!.toFloat(),
                        max = viewModel.maxTemp.value!!.toFloat(),
                        onOk = { value -> onOkClick(value) },
                        onCancel = { onCancelClick() }
                    )
                }
            }
        }
    }

    private fun onOkClick(value: Float) {
        val temp = value.toInt()
        viewModel.setTemp(temp)
        dismiss()
    }

    private fun onCancelClick() {
        dismiss()
    }

    override fun dismiss() {
        lifecycleScope.launch {
            delay(500L)
            super.dismiss()
        }
    }
}