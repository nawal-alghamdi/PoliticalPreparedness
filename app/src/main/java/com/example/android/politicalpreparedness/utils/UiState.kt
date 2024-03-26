package com.example.android.politicalpreparedness.utils

import com.example.android.politicalpreparedness.network.models.Election

/**
 * @Author: nawalalghamdi
 * @Date: 17/09/2023
 */
data class UiState(
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    var electionEvent: ElectionEvent? = null
)

data class ElectionEvent(val election: Election)
