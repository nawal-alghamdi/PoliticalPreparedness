package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.MyApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    //Add ViewModel values and create ViewModel
    private val _viewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory((requireContext().applicationContext as MyApplication).electionsRepository,
            (requireContext().applicationContext as MyApplication).electionDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View {

        // Add binding values
        val binding: FragmentVoterInfoBinding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_voter_info, container, false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = _viewModel
        binding.stateLocations.movementMethod = LinkMovementMethod.getInstance()
        binding.stateBallot.movementMethod = LinkMovementMethod.getInstance()

        val election = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElection
        binding.electionName.title = election.name
        binding.electionDate.text = election.electionDay.toString()

        val isUpcomingElection = VoterInfoFragmentArgs.fromBundle(requireArguments()).isUpcomingElection

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

        // TODO: Handle save button UI state
        // TODO: cont'd Handle save button clicks
        return binding.root
    }

}