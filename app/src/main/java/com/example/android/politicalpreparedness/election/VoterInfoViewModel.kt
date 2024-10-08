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
import timber.log.Timber

class VoterInfoViewModel(
    private val electionsRepository: ElectionsRepository,
    private val election: Election
) : ViewModel() {

    //Add live data to hold voter info
    val voterInfo = MutableLiveData<State>()
    private var isElectionSaved: Boolean = false
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

                is Result.Error -> {
                    Timber.e("voterInfoResult: $voterInfoResult")
                }
            }
        }
    }

    // Add var and methods to save and remove elections to local database
    fun followElectionClicked() {
        viewModelScope.launch {
            isElectionSaved = if (isElectionSaved) {
                electionsRepository.deleteElectionById(election.id)
                _followButtonText.emit(R.string.follow_election)
                false
            } else {
                electionsRepository.saveElection(election)
                _followButtonText.emit(R.string.unfollow_election)
                true
            }
        }
    }

    // Populate initial state of save button to reflect proper action based on election saved status
    private fun initFollowButtonText() {
        viewModelScope.launch {
            val election = electionsRepository.getSavedElectionById(election.id)
            if (election != null) {
                _followButtonText.emit(R.string.unfollow_election)
                isElectionSaved = true
            } else {
                _followButtonText.emit(R.string.follow_election)
                isElectionSaved = false
            }
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}