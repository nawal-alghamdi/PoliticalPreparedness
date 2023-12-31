package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionBinding

    // TODO: Declare ViewModel
    private val _viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory((requireContext().applicationContext as MyApplication).electionsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // TODO: Add ViewModel values and create ViewModel
        // TODO: Add binding values
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        // TODO: Link elections to voter info

        // TODO: Initiate recycler adapters
        // TODO: Populate recycler adapters
        binding.upcomingRecycler.adapter =
            ElectionListAdapter(ElectionListener {
                // for now it will navigate and show text message "true"
                _viewModel.electionClicked(it, true)
            })

        binding.SavedRecycler.adapter = ElectionListAdapter(ElectionListener {
            // for now it will navigate and show text message "false"
            _viewModel.electionClicked(it, false)
        })

        viewLifecycleOwner.lifecycleScope.launch {
            _viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { uiState ->
                    uiState.userMessage?.let { userMessage ->
                        view?.let {
                            Snackbar.make(it, userMessage, BaseTransientBottomBar.LENGTH_LONG)
                                .show()
                        }
                        _viewModel.userMessageShown()
                    }
                    uiState.electionEvent?.let { electionEvent ->
                        findNavController().navigate(
                            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                                electionEvent.election.id,
                                electionEvent.election.division,
                                electionEvent.isUpcomingElection
                            )
                        )
                    }
                }
        }
        return binding.root
    }

    // TODO: Refresh adapters when fragment loads

}