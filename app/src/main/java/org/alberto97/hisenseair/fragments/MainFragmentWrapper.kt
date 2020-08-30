package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import org.alberto97.hisenseair.CompatExtensions.setVisible
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.FragmentMainBinding
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragmentWrapper : Fragment() {

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.splashFragment, R.id.loginFragment, R.id.mainFragment))
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        viewModel.fetchData()

        viewModel.isLoading.observe(viewLifecycleOwner, {
            binding.spinner.spinner.setVisible(it)
        })

        viewModel.isLoggedOut.observe(viewLifecycleOwner, {
            if (it) navigateToLogin()
        })

        return binding.root
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }
}