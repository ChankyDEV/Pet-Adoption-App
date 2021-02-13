package com.example.rokpsia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetBehavior(var active:Float,
                       var barking:Float,
                       var goodBoy:Float,
                       var appetite:Float): Parcelable {

    constructor():this(0.0f,0.0f,0.0f,0.0f)
}