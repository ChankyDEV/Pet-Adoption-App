package com.example.rokpsia.utils

import android.content.Context
import com.example.rokpsia.db.PetsDatabase
import com.example.rokpsia.pocztaPolskaAPI.PolishPostService
import com.example.rokpsia.repository.PetRepository

object Helper {

    fun provideRepository(context: Context): PetRepository {
        return PetRepository(LocationProvider(context),PetsDatabase(context.applicationContext).favPets(),PolishPostService())
    }

}