package org.alberto97.hisenseair.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.hisenseair.BottomSheetFragments
import org.alberto97.hisenseair.ITempUtils
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.TempBinding
import org.alberto97.hisenseair.features.TempType
import org.koin.android.ext.android.inject

class TempSheet :  BottomSheetDialogFragment() {

    private lateinit var binding: TempBinding
    private var value = 32


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TempBinding.inflate(inflater, container, false)

        value = arguments?.getInt("current") ?: value
        val type = arguments?.getInt("tempType") ?: TempType.Fahrenheit.value
        val tempType = TempType.from(type)!!

        val tempUtils: ITempUtils by inject()
        binding.slider.valueTo = tempUtils.getMax(tempType).toFloat()
        binding.slider.valueFrom = tempUtils.getMin(tempType).toFloat()
        binding.slider.value = value.toFloat()

        setTemp(value)

        binding.slider.addOnChangeListener { _, value, _ ->
            setTemp(value.toInt())
        }

        binding.cancel.setOnClickListener { this.dismiss() }
        binding.ok.setOnClickListener {
            val intent = Intent()
            intent.putExtra(BottomSheetFragments.TEMP, value)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

            this.dismiss()
        }

        return binding.root
    }

    private fun setTemp(value: Int) {
        this.value = value
        binding.temp.text = getString(R.string.temp, value)
    }
}