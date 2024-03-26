package com.example.android.politicalpreparedness.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val electionsRepository: ElectionsRepository,
    private val election: Election
) : ViewModel() {

    //Add live data to hold voter info
    val voterInfo = MutableLiveData<State>()
    private var isElectionSaved: Election? = null
    private val _followButtonText = MutableSharedFlow<Int>()
    val followButtonText = _followButtonText.asSharedFlow()

    init {
        initFollowButtonText()
    }

    // Add var and methods to populate voter info
    fun getVoterInfo(address: String, electionId: Int) {
        viewModelScope.launch {
            when (val voterInfoResult = electionsRepository.getVoterInfo(address, electionId)) {
                is Result.Success -> {
                    voterInfo.value = voterInfoResult.data.state?.firstOrNull()
                }
                is Result.Error -> {}
            }
        }
    }

    // Add var and methods to save and remove elections to local database
    fun followElectionClicked() {
        viewModelScope.launch {
            if (isElectionSaved != null) {
                electionsRepository.deleteElectionById(election.id)
                _followButtonText.emit(R.string.follow_election)
            } else {
                electionsRepository.saveElection(election)
                _followButtonText.emit(R.string.unfollow_election)
            }
        }
    }

    // Populate initial state of save button to reflect proper action based on election saved status
    private fun initFollowButtonText() {
        viewModelScope.launch {
            isElectionSaved = electionsRepository.getSavedElectionById(election.id)
            if (isElectionSaved != null) {
                _followButtonText.emit(R.string.unfollow_election)
            } else {
                _followButtonText.emit(R.string.follow_election)
            }
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}