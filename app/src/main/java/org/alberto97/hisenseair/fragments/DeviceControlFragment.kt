package org.alberto97.hisenseair.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.android.settingslib.widget.ActionButtonsPreference
import org.alberto97.hisenseair.BottomSheetFragments
import org.alberto97.hisenseair.DeviceActivityRequest
import org.alberto97.hisenseair.PreferenceConstants
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.bottomsheet.DeviceFanSpeedSheet
import org.alberto97.hisenseair.bottomsheet.DeviceWorkModeSheet
import org.alberto97.hisenseair.bottomsheet.TempSheet
import org.alberto97.hisenseair.features.*
import org.alberto97.hisenseair.ui.TempControlPreference
import org.alberto97.hisenseair.viewmodels.DeviceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class DeviceControlFragment : PreferenceFragmentCompat() {

    private val viewModel: DeviceViewModel by sharedViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_device_advanced, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.overScrollMode = View.OVER_SCROLL_NEVER

        setupTempControl()

        // Setup buttons
        findPreference<ActionButtonsPreference>(PreferenceConstants.PREFERENCE_BUTTONS)!!
            .setButton1Text(R.string.device_turn_off)
            .setButton1Icon(R.drawable.round_power_settings_new_24)
            .setButton1OnClickListener {
                viewModel.isLoading.value = true
                viewModel.switchPower()
            }

        setupMode()
        setupFanSpeed()
        setupSettings()
        setupAirFlow()

        // Setup advanced section
        val advanced = findPreference<PreferenceCategory>(PreferenceConstants.PREFERENCE_CATEGORY_ADVANCED)!!
        setupBacklight(advanced)
        setupBoost(advanced)
        setupEco(advanced)
        setupQuiet(advanced)
    }

    private fun setupTempControl() {
        val tempControl = findPreference<TempControlPreference>("tempControl")!!

        // Ambient temp
        viewModel.roomTemp.observe(viewLifecycleOwner, Observer {
            tempControl.setAmbientTemp(it)
        })

        viewModel.temp.observe(viewLifecycleOwner, Observer {
            tempControl.setTemp(it)
        })

        tempControl.setOnTempClickListener {
            val tempType = if (viewModel.useCelsius)
                TempType.Celsius
            else
                TempType.Fahrenheit

            val bundle = Bundle()
            bundle.putInt("current", viewModel.temp.value!!)
            bundle.putInt("tempType", tempType.value)

            val dialog = TempSheet()
            dialog.arguments = bundle
            dialog.setTargetFragment(this, DeviceActivityRequest.TEMP)
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
        viewModel.workState.observe(viewLifecycleOwner, Observer {
            val resId = modeToStringMap[it] ?: R.string.work_mode_unknown
            val drawableId = modeToIconMap[it] ?: R.drawable.round_brightness_7_24

            mode.summary = getString(resId)
            mode.setIcon(drawableId)
        })

        mode.setOnPreferenceClickListener {
            val bundle = Bundle()
            bundle.putInt("current", viewModel.workState.value!!.value)

            val dialog = DeviceWorkModeSheet()
            dialog.arguments = bundle
            dialog.setTargetFragment(this, DeviceActivityRequest.WORK_MODE)
            dialog.showNow(parentFragmentManager, BottomSheetFragments.MODE)
            true
        }
    }

    private fun setupFanSpeed() {
        val fanSpeed = findPreference<Preference>(PreferenceConstants.PREFERENCE_FAN_SPEED)!!

        // Air flow
        viewModel.fanSpeed.observe(viewLifecycleOwner, Observer {
            val resId = fanToStringMap[it] ?: R.string.fan_speed_unknown
            fanSpeed.summary = getString(resId)
        })

        fanSpeed.setOnPreferenceClickListener {
            val bundle = Bundle()
            bundle.putInt("current", viewModel.fanSpeed.value!!.value)

            val dialog = DeviceFanSpeedSheet()
            dialog.arguments = bundle
            dialog.setTargetFragment(this, DeviceActivityRequest.FAN_SPEED)
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

    private fun setupAirFlow() {
        val category = findPreference<PreferenceCategory>(PreferenceConstants.PREFERENCE_CATEGORY_AIRFLOW)!!
        val horizontal = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_AIRFLOW_HORIZONTAL)!!
        val vertical = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_AIRFLOW_VERTICAL)!!

        viewModel.horizontalAirFlow.observe(viewLifecycleOwner, Observer {
            horizontal.isChecked = it
        })
        viewModel.verticalAirFlow.observe(viewLifecycleOwner, Observer {
            vertical.isChecked = it
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

        viewModel.backlight.observe(viewLifecycleOwner, Observer {
            backlight.isChecked = it
        })

        backlight.setOnPreferenceClickListener {
            viewModel.switchBacklight()
            true
        }
    }

    private fun setupEco(category: PreferenceCategory) {
        val eco = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_ADVANCED_ECO)!!

        viewModel.isEco.observe(viewLifecycleOwner, Observer {
            eco.isChecked = it
        })

        eco.setOnPreferenceClickListener {
            viewModel.switchEco()
            true
        }
    }

    private fun setupQuiet(category: PreferenceCategory) {
        val quiet = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_ADVANCED_QUIET)!!

        viewModel.isQuiet.observe(viewLifecycleOwner, Observer {
            quiet.isChecked = it
        })

        quiet.setOnPreferenceClickListener {
            viewModel.switchQuiet()
            true
        }
    }

    private fun setupBoost(category: PreferenceCategory) {
        val boost = category.findPreference<SwitchPreference>(PreferenceConstants.PREFERENCE_ADVANCED_BOOST)!!

        viewModel.isBoost.observe(viewLifecycleOwner, Observer {
            boost.isChecked = it
        })

        boost.setOnPreferenceClickListener {
            viewModel.switchBoost()
            true
        }
    }

    private fun onFanSpeedChange(fanSpeed: FanSpeed) {
        viewModel.setFanSpeed(fanSpeed)
    }

    private fun onWorkModeChange(workMode: WorkMode) {
        viewModel.setMode(workMode)
    }

    private fun onTempChange(temp: Int) {
        viewModel.setTemp(temp)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            DeviceActivityRequest.FAN_SPEED -> {
                val fanSpeed = data!!.getSerializableExtra(BottomSheetFragments.FAN) as FanSpeed
                onFanSpeedChange(fanSpeed)
            }
            DeviceActivityRequest.WORK_MODE -> {
                val workMode = data!!.getSerializableExtra(BottomSheetFragments.MODE) as WorkMode
                onWorkModeChange(workMode)
            }
            DeviceActivityRequest.TEMP -> {
                val temp = data!!.getIntExtra(BottomSheetFragments.TEMP, 0)
                if (temp > 0)
                    onTempChange(temp)
            }
        }
    }
}