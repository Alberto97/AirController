package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.alberto97.hisenseair.CompatExtensions.getCompatDrawable
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.models.AppDevice
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@Suppress("unused")
class MainFragment : PreferenceFragmentCompat() {

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_devices)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.devices.observe(viewLifecycleOwner, { devices ->
            preferenceScreen.removeAll()
            devices.forEach {
                val preference = buildPreference(it)
                preferenceScreen.addPreference(preference)
            }
        })
    }

    private fun buildPreference(device: AppDevice): Preference {
        val text = if (device.available) "Available" else "Offline"
        return Preference(requireContext()).apply {
            title = device.name
            summary = text
            icon = requireContext().getCompatDrawable(R.drawable.ic_circle_air_conditioner_primary)
            setOnPreferenceClickListener {
                if (device.available) {
                    val direction = MainFragmentWrapperDirections.actionMainFragmentToDeviceFragment(device.id)
                    requireView().findNavController().navigate(direction)
                    true
                } else {
                    false
                }
            }
        }
    }
}