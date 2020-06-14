package org.alberto97.hisenseair.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.alberto97.hisenseair.ITempUtils
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.TempBinding
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.fragments.DEVICE_FRAGMENT_TEMP_SHEET
import org.koin.android.ext.android.inject
import java.lang.Exception

class TempSheet :  BottomSheetDialogFragment() {

    private lateinit var binding: TempBinding
    private var value = 32


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TempBinding.inflate(inflater, container, false)

        setupView()

        binding.seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                setTemp(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        })

        binding.cancel.setOnClickListener { this.dismiss() }
        binding.ok.setOnClickListener {
            val intent = Intent()
            intent.putExtra(DEVICE_FRAGMENT_TEMP_SHEET, value)
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

            this.dismiss()
        }

        return binding.root
    }

    private fun setupView() {
        value = arguments?.getInt("current") ?: value
        val type = arguments?.getInt("tempType") ?: TempType.Fahrenheit.value
        val tempType = TempType.from(type)!!

        val tempUtils: ITempUtils by inject()
        binding.seek.max = tempUtils.getMax(tempType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.seek.min = tempUtils.getMin(tempType)
        } else {
            throw Exception()
        }

        binding.seek.progress = value
        setTemp(value)
    }

    private fun setTemp(value: Int) {
        this.value = value
        binding.temp.text = getString(R.string.temp, value)
    }
}