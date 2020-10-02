package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import org.alberto97.hisenseair.BottomSheetFragments
import org.alberto97.hisenseair.CompatExtensions.requireAppActivity
import org.alberto97.hisenseair.CompatExtensions.setVisible
import org.alberto97.hisenseair.MainActivity
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.bottomsheet.DeviceFanSpeedSheet
import org.alberto97.hisenseair.bottomsheet.DeviceWorkModeSheet
import org.alberto97.hisenseair.bottomsheet.TemperatureControlSheet
import org.alberto97.hisenseair.databinding.FragmentDeviceBinding
import org.alberto97.hisenseair.features.fanToStringMap
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.ui.FullscreenLoading
import org.alberto97.hisenseair.ui.devicecontrol.DeviceOff
import org.alberto97.hisenseair.ui.devicecontrol.TemperatureControlStep
import org.alberto97.hisenseair.ui.preferences.Preference
import org.alberto97.hisenseair.ui.preferences.PreferenceCategory
import org.alberto97.hisenseair.ui.preferences.PreferenceDescription
import org.alberto97.hisenseair.ui.preferences.SwitchPreference
import org.alberto97.hisenseair.ui.theme.AppTheme
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceControlFragment : Fragment() {
    private lateinit var binding: FragmentDeviceBinding

    private val args by navArgs<DeviceControlFragmentArgs>()
    private val viewModel: DeviceViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeviceBinding.inflate(inflater)
        binding.toolbar.setupWithNavController(findNavController())

        viewModel.load(args.dsn)

        // Device name
        viewModel.deviceName.observe(viewLifecycleOwner, {
            binding.toolbar.title = it
        })

        if (requireAppActivity<MainActivity>().displayInPanel)
            binding.toolbar.setVisible(false)

        binding.content.setContent {
            AppTheme {
                Scaffold {
                    val isLoading = viewModel.isLoading.observeAsState(true).value
                    if (isLoading)
                        FullscreenLoading()
                    else
                        screenByState()
                }
            }
        }

        return binding.root
    }

    @Composable
    fun screenByState() {
        val isOn = viewModel.isOn.observeAsState(null)
        when (isOn.value) {
            false -> offScreen()
            true -> onScreen()
            else -> FullscreenLoading()
        }
    }

    @Composable
    fun offScreen() {
        val currentTemp by viewModel.roomTemp.observeAsState(-1)
        val currentMode by viewModel.workState.observeAsState()
        val drawableId = modeToIconMap[currentMode] ?: R.drawable.round_brightness_7_24

        DeviceOff(
            modeAsset = vectorResource(drawableId),
            currentTemp = currentTemp,
            onPower = { viewModel.switchPower() }
        )
    }

    @Composable
    fun onScreen() {
        ScrollableColumn {
            buildTempControl()
            buildAmbientTemp()
            buildMode()
            buildFanSpeed()
            buildNightMode()
            buildSettings()
            buildPower()

            buildAirFlow()

            PreferenceCategory("Advanced")
            buildBacklight()
            buildEco()
            buildQuiet()
            buildBoost()
        }
    }

    @Composable
    private fun buildTempControl() {
        val temp = viewModel.temp.observeAsState().value
        if (temp != null)
            TemperatureControlStep(
                temp = temp,
                onTempDown = { viewModel.reduceTemp() },
                onTempUp = { viewModel.increaseTemp() },
                onTempClick = {
                    val dialog = TemperatureControlSheet()
                    dialog.showNow(parentFragmentManager, BottomSheetFragments.TEMP)
                }
            )
    }

    @Composable
    private fun buildAmbientTemp() {
        val temp = viewModel.roomTemp.observeAsState()

        PreferenceDescription(
            text = getString(R.string.device_ambient_temp, temp.value),
            icon = vectorResource(id = R.drawable.ic_thermometer),
        )
    }

    @Composable
    private fun buildMode() {
        val mode = viewModel.workState.observeAsState().value
        val resId = modeToStringMap[mode] ?: R.string.work_mode_unknown
        val drawableId = modeToIconMap[mode] ?: R.drawable.round_brightness_7_24

        Preference(
            title = "Mode",
            summary = stringResource(resId),
            icon = vectorResource(drawableId),
            onClick = {
                val dialog = DeviceWorkModeSheet()
                dialog.showNow(parentFragmentManager, BottomSheetFragments.MODE)
            }
        )
    }

    @Composable
    private fun buildFanSpeed() {
        val fanSpeed = viewModel.fanSpeed.observeAsState().value
        if (fanSpeed != null) {
            val resId = fanToStringMap.getValue(fanSpeed)
            Preference(
                title = "Fan speed",
                summary = stringResource(resId),
                icon = vectorResource(R.drawable.ic_fan),
                onClick = {
                    val dialog = DeviceFanSpeedSheet()
                    dialog.showNow(parentFragmentManager, BottomSheetFragments.FAN)
                }
            )
        }
    }

    @Composable
    private fun buildNightMode() {
        Preference(
            title = "Night mode",
            summary = "Off",
            icon = vectorResource(R.drawable.ic_nights_stay)
        )
    }

    @Composable
    private fun buildSettings() {
        val visible = remember { !requireAppActivity<MainActivity>().displayInPanel }
        if (visible)
            Preference(
                title = "Settings",
                summary = "Temperature unit, device name and info",
                icon = vectorResource(R.drawable.ic_round_settings_24),
                onClick = {
                    val direct = DeviceControlFragmentDirections.deviceControlToDeviceSettings(viewModel.dsn)
                    findNavController().navigate(direct)
                }
            )
    }

    @Composable
    private fun buildPower() {
        SwitchPreference(
            title = "Power",
            summary = "Turn off the device",
            checked = true,
            icon = vectorResource(R.drawable.round_power_settings_new_24),
            onCheckedChange = { viewModel.switchPower() }
        )
    }

    @Composable
    private fun buildAirFlow() {
        val horizontal = viewModel.horizontalAirFlow.observeAsState().value
        val vertical = viewModel.verticalAirFlow.observeAsState().value

        if (horizontal != null || vertical != null)
            PreferenceCategory("Air Flow")

        if (horizontal != null)
            SwitchPreference(
                title = "Horizontal",
                checked = horizontal,
                onCheckedChange = { viewModel.switchAirFlowHorizontal() },
                icon = vectorResource(R.drawable.round_swap_horiz_24)
            )

        if (vertical != null)
            SwitchPreference(
                title = "Vertical",
                checked = vertical,
                onCheckedChange = { viewModel.switchAirFlowVertical() },
                icon = vectorResource(R.drawable.round_swap_vert_24)
            )
    }

    @Composable
    private fun buildBacklight() {
        val backlight = viewModel.backlight.observeAsState().value
        if (backlight != null)
            SwitchPreference(
                title = "Dimmer",
                checked = backlight,
                onCheckedChange = { viewModel.switchBacklight() },
                icon = vectorResource(R.drawable.ic_lightbulb_md)
            )
    }

    @Composable
    private fun buildEco() {
        val eco = viewModel.isEco.observeAsState().value
        if (eco != null)
            SwitchPreference(
                title = "Eco",
                checked = eco,
                onCheckedChange = { viewModel.switchEco() },
                icon = vectorResource(R.drawable.ic_eco)
            )
    }

    @Composable
    private fun buildQuiet() {
        val quiet = viewModel.isQuiet.observeAsState().value
        if (quiet != null)
            SwitchPreference(
                title = "Quiet",
                checked = quiet,
                onCheckedChange = { viewModel.switchQuiet() },
                icon = vectorResource(R.drawable.outline_hearing_disabled_24)
            )
    }

    @Composable
    private fun buildBoost() {
        val boost = viewModel.isBoost.observeAsState().value
        if (boost != null)
            SwitchPreference(
                title = "Super",
                checked = boost,
                onCheckedChange = {viewModel.switchBoost()},
                icon = vectorResource(R.drawable.ic_fan_plus)
            )
    }
}