package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import timber.log.Timber

/**
 * @Author: nawalalghamdi
 * @Date: 11/09/2023
 */
class MyApplication : Application() {

    val electionsRepository: ElectionsRepository
        get() = ServiceLocator.provideElectionsRepository(this)

    val electionDao: ElectionDao
        get() = ServiceLocator.provideElectionDao(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }
}