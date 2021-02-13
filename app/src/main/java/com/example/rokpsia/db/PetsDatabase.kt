package com.example.rokpsia.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rokpsia.models.Pet
import com.example.rokpsia.models.PetRejected


@Database(entities = [Pet::class,PetRejected::class],version=1,exportSchema = false)
abstract class PetsDatabase:RoomDatabase() {

    abstract fun favPets():FavPetsDao
    abstract fun rejectedPets():RejectedPetsDao

    companion object{
        @Volatile private var instance: PetsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                PetsDatabase::class.java, "PetsEntries.db")
                .build()
    }

}