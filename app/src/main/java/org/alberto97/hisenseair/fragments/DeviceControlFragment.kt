package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.alberto97.hisenseair.CompatExtensions.requireAppActivity
import org.alberto97.hisenseair.MainActivity
import org.alberto97.hisenseair.ui.devicecontrol.DeviceControlScreen
import org.alberto97.hisenseair.ui.theme.AppTheme

class DeviceControlFragment : Fragment() {
    private val args by navArgs<DeviceControlFragmentArgs>()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Scaffold {
                        DeviceControlScreen(
                            dsn = args.dsn,
                            displayInPanel = requireAppActivity<MainActivity>().displayInPanel,
                            onSettingsClick = { navigateToSettings() }
                        )
                    }
                }
            }
        }
    }

    private fun navigateToSettings() {
        val direct =
            DeviceControlFragmentDirections.deviceControlToDeviceSettings(args.dsn)
        findNavController().navigate(direct)
    }
}