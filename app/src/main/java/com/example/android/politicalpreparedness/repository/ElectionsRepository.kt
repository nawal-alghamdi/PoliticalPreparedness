package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * @Author: nawalalghamdi
 * @Date: 11/09/2023
 */
class ElectionsRepository (
    private val electionDao: ElectionDao,
    private val retrofitService: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO){

    suspend fun getSavedElection() : List<Election> {
        return electionDao.getElections()
    }

    suspend fun getUpcomingElections() : Result<List<Election>> {
        return try {
            withContext(ioDispatcher) {
               Result.Success(retrofitService.getElections().elections)
            }
        }catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

}