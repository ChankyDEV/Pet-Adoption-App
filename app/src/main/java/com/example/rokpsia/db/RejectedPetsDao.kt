package com.example.rokpsia.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rokpsia.models.PetRejected
import kotlinx.coroutines.flow.Flow

@Dao
interface RejectedPetsDao {

    @Query("SELECT * FROM petsRejected")
    fun getRejectedIds(): Flow<List<PetRejected>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun getNewRejected(rejected: PetRejected)

    @Query("delete from petsRejected where id=:id")
    suspend fun deletePetRejected(id:String)

}