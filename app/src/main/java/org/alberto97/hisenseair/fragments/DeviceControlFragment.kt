package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.alberto97.hisenseair.CompatExtensions.requireAppActivity
import org.alberto97.hisenseair.MainActivity
import org.alberto97.hisenseair.ui.devicecontrol.DeviceControlScreen
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceControlFragment : Fragment() {
    private val args by navArgs<DeviceControlFragmentArgs>()
    private val viewModel: DeviceViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.load(args.dsn)

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Scaffold {
                        DeviceControlScreen(
                            viewModel = viewModel,
                            displayInPanel = requireAppActivity<MainActivity>().displayInPanel,
                            onSettingsClick = { navigateToSettings() },
                            onShowTempControlPanel = { onShowTempControlPanel() },
                            onShowModePanel = { onShowModePanel() },
                            onShowFanSpeedPanel = { onShowFanSpeedPanel() }
                        )
                    }
                }
            }
        }
    }

    private fun onShowTempControlPanel() {
        val direction = DeviceControlFragmentDirections.actionDeviceControlTemp()
        findNavController().navigate(direction)
    }

    private fun onShowModePanel() {
        val direction = DeviceControlFragmentDirections.actionDeviceControlMode()
        findNavController().navigate(direction)
    }

    private fun onShowFanSpeedPanel() {
        val direction = DeviceControlFragmentDirections.actionDeviceControlFanSpeed()
        findNavController().navigate(direction)
    }

    private fun navigateToSettings() {
        val direct =
            DeviceControlFragmentDirections.deviceControlToDeviceSettings(viewModel.dsn)
        findNavController().navigate(direct)
    }
}