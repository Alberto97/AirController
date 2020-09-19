package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
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
                build()
            }
        }
    }

    @Composable
    private fun build() {
        val scaffoldState =  rememberScaffoldState()
        val snackScope = rememberCoroutineScope()

        val isAuthenticated by viewModel.isAuthenticated.observeAsState()
        when (isAuthenticated) {
            false -> snackScope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Login failed")
            }
            true -> findNavController().navigate(LoginFragmentDirections.loginToMain())
        }

        LoginScreen(
            scaffoldState = scaffoldState,
            onLogin = { email, password -> viewModel.login(email, password) }
        )
    }
}