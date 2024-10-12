package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi.retrofitService
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch

class RepresentativeViewModel(state: SavedStateHandle) : ViewModel() {

    // Establish live data for representatives and address
    private val _representatives = state.getLiveData<List<Representative>>(REPRESENTATIVES_STATE_KEY)
    val representatives = _representatives
    private val _address = state.getLiveData<Address>(ADDRESS_STATE_KEY)
    val address = _address

    // Create function to fetch representatives from API from a provided address
    fun fetchRepresentative() {
        if (address.value != null) {
            try {
                viewModelScope.launch {
                    val (offices, officials) = Result.Success(
                        retrofitService.getRepresentatives(
                            address.value!!.toFormattedString()
                        )
                    ).data
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }
                }
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }
    }

    // Create function get address from geo location
    fun updateAddress(address: Address) {
        _address.value = address
    }

    // Create function to get address from individual fields
    fun updateAddress(
        line1: String,
        line2: String? = null,
        city: String,
        state: String,
        zip: String
    ) {
        _address.value = Address(line1, line2, city, state, zip)
    }

    companion object {
        private const val REPRESENTATIVES_STATE_KEY: String = "representative_state_key"
        private const val ADDRESS_STATE_KEY: String = "address_state_key"
        const val MOTION_LAYOUT_STATE_KEY: String = "motion_layout_state_key"
        const val IS_KEYBOARD_VISIBLE_KEY: String = "is_keyboard_visible_key"
    }

}