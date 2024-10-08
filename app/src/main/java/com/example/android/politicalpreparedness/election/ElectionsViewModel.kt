package com.example.android.politicalpreparedness.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.utils.ElectionEvent
import com.example.android.politicalpreparedness.utils.Result
import com.example.android.politicalpreparedness.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val electionsRepository: ElectionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadElections()
    }

    // Create live data val for upcoming elections
    val upcomingElections = MutableLiveData<List<Election>>()

    // Create live data val for saved elections
    val savedElections = MutableLiveData<List<Election>>()

    // Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun loadElections() {
        viewModelScope.launch {
            when (val upcomingElectionResult = electionsRepository.getUpcomingElections()) {
                is Result.Success -> {
                    upcomingElections.value = upcomingElectionResult.data
                }

                is Result.Error -> {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(userMessage = upcomingElectionResult.message)
                    }
                }
            }
        }
    }

    fun getSavedElection() {
        viewModelScope.launch {
            savedElections.value = electionsRepository.getSavedElection()
        }
    }

    fun userMessageShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessage = null)
        }
    }

    // Create functions to navigate to saved or upcoming election voter info
    fun electionClicked(election: Election) {
        _uiState.update { currentUiState ->
            currentUiState.copy(electionEvent = ElectionEvent(election))
        }
    }

}