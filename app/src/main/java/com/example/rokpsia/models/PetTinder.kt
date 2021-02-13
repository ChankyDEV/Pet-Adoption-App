package com.example.rokpsia.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PetTinder(val uri: Uri, val pet:Pet):Parcelable {
}