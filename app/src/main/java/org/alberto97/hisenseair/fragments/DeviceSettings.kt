package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import org.alberto97.hisenseair.CompatExtensions.setVisible
import org.alberto97.hisenseair.databinding.FragmentSettingsBinding
import org.alberto97.hisenseair.viewmodels.DevicePreferenceViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeviceSettings : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val args by navArgs<DeviceSettingsArgs>()
    private val viewModel: DevicePreferenceViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        binding.toolbar.setupWithNavController(findNavController())

        viewModel.load(args.dsn)

        viewModel.deviceName.observe(viewLifecycleOwner, {
            binding.spinner.spinner.setVisible(false)
        })

        return  binding.root
    }
}