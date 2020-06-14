package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreference
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.viewmodels.DevicePreferenceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DevicePreferenceFragment : ToolbarPreferenceFragment() {

    private val args by navArgs<DevicePreferenceFragmentArgs>()
    private val viewModel: DevicePreferenceViewModel by viewModel  { parametersOf(args.dsn) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_device_settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<FrameLayout>(android.R.id.list_container)
        val spinner = view.findViewById<ProgressBar>(R.id.spinner)
        val general = findPreference<PreferenceCategory>("general")

        // Name
        val deviceName = general?.findPreference<EditTextPreference>("deviceName")
        viewModel.deviceName.observe(viewLifecycleOwner, Observer {
            list.visibility = View.VISIBLE
            spinner.visibility = View.GONE

            deviceName?.summary = it
            deviceName?.text = it
        })

        deviceName?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.updateDeviceName(newValue as String)
            true
        }

        // Temp
        val tempType = general?.findPreference<SwitchPreference>("useCelsius")
        viewModel.tempType.observe(viewLifecycleOwner, Observer {
            tempType?.isChecked = it == TempType.Celsius
        })
        tempType?.setOnPreferenceClickListener {
            viewModel.switchTempType()
            true
        }

        val info = findPreference<PreferenceCategory>("info")

        // MAC Address
        val macAddress = info?.findPreference<Preference>("macAddress")
        viewModel.mac.observe(viewLifecycleOwner, Observer {
            macAddress?.summary = it
        })

        // IP Address
        val ipAddress = info?.findPreference<Preference>("ipAddress")
        viewModel.ip.observe(viewLifecycleOwner, Observer {
            ipAddress?.summary = it
        })

        // SSID
        val ssid = info?.findPreference<Preference>("ssid")
        viewModel.ssid.observe(viewLifecycleOwner, Observer {
            ssid?.summary = it
        })
    }
}