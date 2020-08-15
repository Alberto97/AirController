package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import org.alberto97.hisenseair.CompatExtensions.setVisible
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.FragmentDeviceBinding
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceFragment : Fragment() {
    lateinit var binding: FragmentDeviceBinding

    private val args by navArgs<DeviceFragmentArgs>()
    private val viewModel: DeviceViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeviceBinding.inflate(layoutInflater)
        binding.toolbar.setupWithNavController(findNavController())
        setSpinnerVisible(true)

        viewModel.load(args.dsn)

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.spinner.spinner.setVisible(it)
        })

        binding.offLayout.power.setOnClickListener {
            setSpinnerVisible(true)
            viewModel.switchPower()
        }

        viewModel.isOn.observe(viewLifecycleOwner, Observer {
            setSpinnerVisible(false)
            updateLayoutVisibility(it)
        })

        // Offline ambient temp
        viewModel.roomTemp.observe(viewLifecycleOwner, Observer {
            binding.offLayout.roomTemp.text = getString(R.string.device_room_temp, it)
        })

        // Offline work mode
        viewModel.workState.observe(viewLifecycleOwner, Observer {
            val drawableId = modeToIconMap[it] ?: R.drawable.round_brightness_7_24
            binding.offLayout.offMode.setImageResource(drawableId)
        })

        // Device name
        viewModel.deviceName.observe(viewLifecycleOwner, Observer {
            binding.toolbar.title = it
        })

        return binding.root
    }

    private fun updateLayoutVisibility(isOn: Boolean) {
        binding.offLayout.offLayout.setVisible(!isOn)
        binding.advanced.setVisible(isOn)
    }

    private fun setSpinnerVisible(visible: Boolean) {
        viewModel.isLoading.value = visible
    }
}