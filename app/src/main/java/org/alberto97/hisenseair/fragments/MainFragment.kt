package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.FragmentMainBinding
import org.alberto97.hisenseair.ui.devices.DevicesScreen
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.splashFragment, R.id.loginFragment, R.id.mainFragment))
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        binding.content.setContent {
            val loggedOut by viewModel.isLoggedOut.observeAsState(false)
            if (loggedOut)
                findNavController().navigate(R.id.loginFragment)

            DevicesScreen(
                viewModel = viewModel,
                onDeviceClick = { device -> onDeviceClick(device) }
            )
        }

        return binding.root
    }

    private fun onDeviceClick(id: String) {
        val direction = MainFragmentDirections.actionMainFragmentToDeviceFragment(id)
        findNavController().navigate(direction)
    }
}