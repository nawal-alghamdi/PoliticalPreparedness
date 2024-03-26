package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import kotlinx.coroutines.flow.collectLatest

class VoterInfoFragment : Fragment() {

    private val args: VoterInfoFragmentArgs by navArgs()

    //Add ViewModel values and create ViewModel
    private val _viewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory(
            (requireContext().applicationContext as MyApplication).electionsRepository,
            args.argElection
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View {

        // Add binding values
        val binding: FragmentVoterInfoBinding = DataBindingUtil.inflate<FragmentVoterInfoBinding?>(
            inflater, R.layout.fragment_voter_info, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = _viewModel
            stateLocations.movementMethod = LinkMovementMethod.getInstance()
            stateBallot.movementMethod = LinkMovementMethod.getInstance()
        }

        val election = args.argElection
        binding.electionName.title = election.name
        binding.electionDate.text = election.electionDay.toString()

        lifecycleScope.launchWhenStarted {
            _viewModel.followButtonText.collectLatest { buttonTextId ->
                binding.followButton.text = getString(buttonTextId)
            }
        }

        // Populate voter info -- hide views without provided data.
        val address = "${election.division.state}, ${election.division.country}"
        _viewModel.getVoterInfo(address, election.id)

       _viewModel.voterInfo.observe(viewLifecycleOwner) {
           if (it.electionAdministrationBody.correspondenceAddress != null) {
               binding.stateCorrespondenceHeader.visibility = View.VISIBLE
               binding.stateCorrespondenceHeader.text = getString(R.string.mailing_address)
               binding.address.visibility = View.VISIBLE
           } else {
               binding.stateCorrespondenceHeader.visibility = View.GONE
               binding.address.visibility = View.GONE
           }
       }

        return binding.root
    }

}