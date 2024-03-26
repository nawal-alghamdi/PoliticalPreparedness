package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository

// Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val electionsRepository: ElectionsRepository,
                                private val election: Election
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VoterInfoViewModel(electionsRepository, election) as T
    }

}