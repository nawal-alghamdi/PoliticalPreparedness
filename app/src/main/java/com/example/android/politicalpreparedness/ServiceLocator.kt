package com.example.android.politicalpreparedness

import android.content.Context
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.repository.ElectionsRepository

/**
 * @Author: nawalalghamdi
 * @Date: 11/09/2023
 */
object ServiceLocator {

    @Volatile
    var electionsRepository: ElectionsRepository? = null
    var electionDao: ElectionDao? = null

    fun provideElectionsRepository(context: Context): ElectionsRepository {
        synchronized(this) {
            return electionsRepository ?: createElectionsRepository(context)
        }
    }

    private fun createElectionsRepository(context: Context): ElectionsRepository {
        val newRepo = ElectionsRepository(
            ElectionDatabase.getInstance(context).electionDao,
            CivicsApi.retrofitService
        )
        electionsRepository = newRepo
        return newRepo
    }

    fun provideElectionDao(context: Context): ElectionDao {
        synchronized(this) {
            if (electionDao == null) {
                electionDao = ElectionDatabase.getInstance(context).electionDao
            }
            return electionDao as ElectionDao
        }
    }

}