package com.example.rokpsia.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "petsRejected")
data class PetFavourite(@PrimaryKey(autoGenerate = false)
                       var id:String) {
    constructor():this("")
}


