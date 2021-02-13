package com.example.rokpsia.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rokpsia.models.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface FavPetsDao {

    @Query("SELECT * FROM petsFav")
    fun getFavPets(): Flow<List<Pet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet:Pet)

    @Query("delete from petsFav where id=:id")
    suspend fun deleteFavPet(id:String)


}