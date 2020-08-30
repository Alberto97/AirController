package org.alberto97.hisenseair.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.TempBinding
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TempSheet :  BottomSheetDialogFragment() {

    private val viewModel: DeviceViewModel by sharedViewModel()
    private lateinit var binding: TempBinding
    private var selectedTemp = 32


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TempBinding.inflate(inflater, container, false)

        selectedTemp = viewModel.temp.value!!
        binding.slider.valueTo = viewModel.maxTemp.value!!.toFloat()
        binding.slider.valueFrom = viewModel.minTemp.value!!.toFloat()
        binding.slider.value = selectedTemp.toFloat()

        updateTempLabel()

        binding.slider.addOnChangeListener { _, value, _ ->
            selectedTemp = value.toInt()
            updateTempLabel()
        }

        binding.cancel.setOnClickListener { this.dismiss() }
        binding.ok.setOnClickListener {
            viewModel.setTemp(selectedTemp)
            dismiss()
        }

        return binding.root
    }

    private fun updateTempLabel() {
        binding.temp.text = getString(R.string.temp, selectedTemp)
    }
}