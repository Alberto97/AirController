package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.*
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.viewmodels.DevicePreferenceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

@Suppress("unused")
class DevicePreferenceFragment : PreferenceFragmentCompat() {

    // Fragment arguments containing navigation parameters are unavailable here
    // but that does not matter because the vm has already been instantiated
    // and it is bounded to the activity lifecycle.
    private val viewModel: DevicePreferenceViewModel by sharedStateViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_device_settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val general = findPreference<PreferenceCategory>("general")

        // Name
        val deviceName = general?.findPreference<EditTextPreference>("deviceName")
        viewModel.deviceName.observe(viewLifecycleOwner, {
            deviceName?.summary = it
            deviceName?.text = it
        })

        deviceName?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.updateDeviceName(newValue as String)
            true
        }

        // Temp
        val tempType = general?.findPreference<SwitchPreference>("useCelsius")
        viewModel.useCelsius.observe(viewLifecycleOwner, {
            tempType?.isChecked = it
        })
        tempType?.setOnPreferenceClickListener {
            viewModel.switchTempType()
            true
        }

        // Delete device
        val deleteDevice = general?.findPreference<Preference>("deleteDevice")
        deleteDevice?.setOnPreferenceClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Delete device")
                setMessage("Delete the device from your account?")
                setNegativeButton(android.R.string.cancel) { _, _ -> }
                setPositiveButton(android.R.string.ok) { _, _ ->
                    viewModel.deleteDevice()
                }
            }.show()
            true
        }
        viewModel.popToHome.observe(viewLifecycleOwner, { popToHome ->
            // TODO: Figure out how to close device control screen
            if (popToHome)
                requireActivity().finish()
        })

        val info = findPreference<PreferenceCategory>("info")

        // MAC Address
        val macAddress = info?.findPreference<Preference>("macAddress")
        viewModel.mac.observe(viewLifecycleOwner, {
            macAddress?.summary = it
        })

        // IP Address
        val ipAddress = info?.findPreference<Preference>("ipAddress")
        viewModel.ip.observe(viewLifecycleOwner, {
            ipAddress?.summary = it
        })

        // SSID
        val ssid = info?.findPreference<Preference>("ssid")
        viewModel.ssid.observe(viewLifecycleOwner, {
            ssid?.summary = it
        })
    }
}