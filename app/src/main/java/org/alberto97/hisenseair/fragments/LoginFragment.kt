package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.alberto97.hisenseair.ui.login.LoginScreen
import org.alberto97.hisenseair.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalMaterialApi
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                LoginScreen(
                    viewModel = viewModel,
                    onAuthenticated = { findNavController().navigate(LoginFragmentDirections.loginToMain()) }
                )
            }
        }
    }
}