package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import org.alberto97.hisenseair.BottomSheetFragments
import org.alberto97.hisenseair.PreferenceConstants
import org.alberto97.hisenseair.PreferenceExtensions.setCheckedIfVisible
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.bottomsheet.DeviceFanSpeedSheet
import org.alberto97.hisenseair.bottomsheet.DeviceWorkModeSheet
import org.alberto97.hisenseair.bottomsheet.TemperatureControlSheet
import org.alberto97.hisenseair.features.fanToStringMap
import org.alberto97.hisenseair.features.modeToIconMap
import org.alberto97.hisenseair.features.modeToStringMap
import org.alberto97.hisenseair.ui.TempControlPreference
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@Suppress("unused")
class DeviceControlFragment : PreferenceFragmentCompat() {

    private val viewModel: DeviceViewModel by sharedViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_device_advanced, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.overScrollMode = View.OVER_SCROLL_NEVER

        setupTempControl()
        setupMode()
        setupFanSpeed()
        setupSettings()
        setupPower()
        setupAirFlow()

        // Setup advanced section
        val advanced = findPreference<PreferenceCategory>(PreferenceConstants.PREFERENCE_CATEGORY_ADVANCED)!!
        setupBacklight(advanced)
        setupBoost(advanced)
        setupEco(advanced)
        setupQuiet(advanced)
    }

    private fun setupTempControl() {
        val ambientTemp = findPreference<Preference>(PreferenceConstants.PREFERENCE_AMBIENT_TEMP)!!
        val tempControl = findPreference<TempControlPreference>(PreferenceConstants.PREFERENCE_TEMP_CONTROL)!!

        // Ambient temp
        viewModel.roomTemp.observe(viewLifecycleOwner, {
            ambientTemp.summary = getString(R.string.device_ambient_temp, it)
        })

        viewModel.temp.observe(viewLifecycleOwner, {
            tempControl.isVisible = it != null
            if (tempControl.isVisible)
                tempControl.setTemp(it)
        })

        tempControl.setOnTempClickListener {
            val dialog = TemperatureControlSheet()
            dialog.showNow(parentFragmentManager, BottomSheetFragments.TEMP)
        }

        tempControl.setOnTempDownClickListener {
            viewModel.reduceTemp()
        }
        tempControl.setOnTempUpClickListener {
            viewModel.increaseTemp()
        }
    }

    private fun setupMode() {
        val mode = findPreference<Preference>(PreferenceConstants.PREFERENCE_MODE)!!

        // Work mode
        viewModel.workState.observe(viewLifecycleOwner, {
            val resId = modeToStringMap[it] ?: R.string.work_mode_unknown
            val drawableId = modeToIconMap[it] ?: R.drawable.round_brightness_7_24

            mode.summary = getString(resId)
            mode.setIcon(drawableId)
        })

        mode.setOnPreferenceClickListener {
            val dialog = DeviceWorkModeSheet()
            dialog.showNow(parentFragmentManager, BottomSheetFragments.MODE)
            true
        }
    }

    private fun setupFanSpeed() {
        val fanSpeed = findPreference<Preference>(PreferenceConstants.PREFERENCE_FAN_SPEED)!!

        // Air flow
        viewModel.fanSpeed.observe(viewLifecycleOwner, {
            fanSpeed.isVisible = it != null
            if (fanSpeed.isVisible) {
                val resId = fanToStringMap.getValue(it)
                fanSpeed.summary = getString(resId)
            }
        })

        fanSpeed.setOnPreferenceClickListener {
            val dialog = DeviceFanSpeedSheet()
            dialog.showNow(parentFragmentManager, BottomSheetFragments.FAN)
            true
        }
    }

    private fun setupSettings() {
        val settings = findPreference<Preference>(PreferenceConstants.PREFERENCE_SETTINGS)!!
        settings.setOnPreferenceClickListener {
            val direct = DeviceFragmentDirections.actionDeviceFragmentNewToDevicePreferenceFragment(viewModel.dsn)
            findNavController().navigate(direct)
            true
        }

    }

    private fun setupPower() {
        val power = findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_POWER)!!

        viewModel.isOn.observe(viewLifecycleOwner, {
            power.isChecked = it
        })

        power.setOnPreferenceClickListener {
            viewModel.isLoading.value = true
            viewModel.switchPower()
            true
        }

    }

    private fun setupAirFlow() {
        val category = findPreference<PreferenceCategory>(PreferenceConstants.PREFERENCE_CATEGORY_AIRFLOW)!!
        val horizontal = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_AIRFLOW_HORIZONTAL)!!
        val vertical = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_AIRFLOW_VERTICAL)!!

        viewModel.horizontalAirFlow.observe(viewLifecycleOwner, {
            horizontal.setCheckedIfVisible(it)
        })
        viewModel.verticalAirFlow.observe(viewLifecycleOwner, {
            vertical.setCheckedIfVisible(it)
        })

        horizontal.setOnPreferenceClickListener {
            viewModel.switchAirFlowHorizontal()
            true
        }
        vertical.setOnPreferenceClickListener {
            viewModel.switchAirFlowVertical()
            true
        }
    }

    private fun setupBacklight(category: PreferenceCategory) {
        val backlight = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_ADVANCED_BACKLIGHT)!!

        viewModel.backlight.observe(viewLifecycleOwner, {
            backlight.setCheckedIfVisible(it)
        })

        backlight.setOnPreferenceClickListener {
            viewModel.switchBacklight()
            true
        }
    }

    private fun setupEco(category: PreferenceCategory) {
        val eco = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_ADVANCED_ECO)!!

        viewModel.isEco.observe(viewLifecycleOwner, {
            eco.setCheckedIfVisible(it)
        })

        eco.setOnPreferenceClickListener {
            viewModel.switchEco()
            true
        }
    }

    private fun setupQuiet(category: PreferenceCategory) {
        val quiet = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_ADVANCED_QUIET)!!

        viewModel.isQuiet.observe(viewLifecycleOwner, {
            quiet.setCheckedIfVisible(it)
        })

        quiet.setOnPreferenceClickListener {
            viewModel.switchQuiet()
            true
        }
    }

    private fun setupBoost(category: PreferenceCategory) {
        val boost = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_ADVANCED_BOOST)!!

        viewModel.isBoost.observe(viewLifecycleOwner, {
            boost.setCheckedIfVisible(it)
        })

        boost.setOnPreferenceClickListener {
            viewModel.switchBoost()
            true
        }
    }
}