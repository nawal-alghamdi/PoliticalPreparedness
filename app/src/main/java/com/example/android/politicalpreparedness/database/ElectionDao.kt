package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(election: Election)

    //TODO: Add select all election query
    @Query("select * from election_table")
    suspend fun getElections(): List<Election>

    //TODO: Add select single election query
    @Query("select * from election_table where id = :electionId")
    suspend fun getElectionById(electionId: Int): Election?

    //TODO: Add delete query
    @Query("delete from election_table where id = :electionId")
    suspend fun deleteElectionById(electionId: Int)

    //TODO: Add clear query
    @Query("delete from election_table")
    suspend fun deleteAllElections()

}