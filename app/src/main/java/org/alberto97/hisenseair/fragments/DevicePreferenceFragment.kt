package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.*
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.features.TempType
import org.alberto97.hisenseair.viewmodels.DevicePreferenceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@Suppress("unused")
class DevicePreferenceFragment : PreferenceFragmentCompat() {

    private val viewModel: DevicePreferenceViewModel by sharedViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_device_settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val general = findPreference<PreferenceCategory>("general")

        // Name
        val deviceName = general?.findPreference<EditTextPreference>("deviceName")
        viewModel.deviceName.observe(viewLifecycleOwner, Observer {
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