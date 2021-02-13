package com.example.rokpsia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetCharacter(var calm:Float,
                        var fearful:Float,
                        var smart:Float,
                        var family:Float): Parcelable {

    constructor():this(0.0f,0.0f,0.0f,0.0f)
}