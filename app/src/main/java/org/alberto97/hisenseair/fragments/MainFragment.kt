package org.alberto97.hisenseair.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.alberto97.hisenseair.adapters.DevicesAdapter
import org.alberto97.hisenseair.R
import org.alberto97.hisenseair.databinding.FragmentMainBinding
import org.alberto97.hisenseair.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchData()
        }

        val listAdapter = DevicesAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = listAdapter
        }

        viewModel.devices.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = false
            listAdapter.submitList(it)
        })

        viewModel.isLoggedOut.observe(viewLifecycleOwner, Observer {
            if (it) navigateToLogin()
        })

        return binding.root
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }
}