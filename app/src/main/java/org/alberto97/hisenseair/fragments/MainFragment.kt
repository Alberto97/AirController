package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.alberto97.hisenseair.R
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

        return ComposeView(requireContext()).apply {
            setContent {
                val loggedOut by viewModel.isLoggedOut.observeAsState(false)
                if (loggedOut)
                    findNavController().navigate(R.id.loginFragment)

                DevicesScreen(
                    viewModel = viewModel,
                    onDeviceClick = { device -> onDeviceClick(device) }
                )
            }
        }
    }

    private fun onDeviceClick(id: String) {
        val direction = MainFragmentDirections.mainToDeviceControl(id)
        findNavController().navigate(direction)
    }
}