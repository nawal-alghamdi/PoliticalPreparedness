package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.repository.ElectionsRepository

/**
 * @Author: nawalalghamdi
 * @Date: 11/09/2023
 */
class MyApplication : Application() {

    val electionsRepository: ElectionsRepository
        get() = ServiceLocator.provideElectionsRepository(this)

    override fun onCreate() {
        super.onCreate()
    }
}