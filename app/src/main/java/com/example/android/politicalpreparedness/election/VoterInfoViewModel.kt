package com.example.android.politicalpreparedness.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch

// I need data source to save the voter info when clicked
class VoterInfoViewModel(private val dataSource: ElectionDao,
                         private val electionsRepository: ElectionsRepository
) : ViewModel() {

    //Add live data to hold voter info
    val voterInfo = MutableLiveData<State>()

    // Add var and methods to populate voter info
    fun getVoterInfo(address: String, electionId: Int) {
        viewModelScope.launch {
            when (val voterInfoResult = electionsRepository.getVoterInfo(address, electionId)) {
                is Result.Success -> {
                    voterInfo.value = voterInfoResult.data.state?.firstOrNull()
                }

                is Result.Error -> {

                    }
                }

            }
        }



    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}