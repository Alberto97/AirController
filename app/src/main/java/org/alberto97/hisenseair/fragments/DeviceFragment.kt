package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import org.alberto97.hisenseair.CompatExtensions.requireAppActivity
import org.alberto97.hisenseair.CompatExtensions.setVisible
import org.alberto97.hisenseair.MainActivity
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.FragmentDeviceBinding
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.ui.devicecontrol.DeviceOff
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceFragment : Fragment() {
    private lateinit var binding: FragmentDeviceBinding

    private val args by navArgs<DeviceFragmentArgs>()
    private val viewModel: DeviceViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeviceBinding.inflate(inflater)
        binding.toolbar.setupWithNavController(findNavController())

        viewModel.load(args.dsn)

        if (requireAppActivity<MainActivity>().displayInPanel)
            binding.toolbar.setVisible(false)

        viewModel.isLoading.observe(viewLifecycleOwner, {
            binding.spinner.spinner.setVisible(it)
        })

        viewModel.isOn.observe(viewLifecycleOwner, {
            updateLayoutVisibility(it)
        })

        // Device name
        viewModel.deviceName.observe(viewLifecycleOwner, {
            binding.toolbar.title = it
        })

        binding.offLayout.setContent {
            val currentTemp by viewModel.roomTemp.observeAsState(-1)
            val currentMode by viewModel.workState.observeAsState()
            val drawableId = modeToIconMap[currentMode] ?: R.drawable.round_brightness_7_24

            AppTheme {
                Surface {
                    DeviceOff(
                        modeAsset = vectorResource(drawableId),
                        currentTemp = currentTemp,
                        onPower = { onPowerClick() }
                    )
                }
            }
        }
        return binding.root
    }

    private fun onPowerClick() {
        viewModel.switchPower()
    }

    private fun updateLayoutVisibility(isOn: Boolean) {
        binding.offLayout.setVisible(!isOn)
        binding.advanced.setVisible(isOn)
    }
}