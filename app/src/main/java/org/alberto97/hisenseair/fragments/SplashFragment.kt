package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.alberto97.hisenseair.databinding.FragmentSplashBinding
import org.alberto97.hisenseair.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSplashBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val direction =
            if (viewModel.isLoggedIn()) {
                SplashFragmentDirections.splashToLogin()
            } else {
                SplashFragmentDirections.splashToMain()
            }

        findNavController().navigate(direction)
    }
}