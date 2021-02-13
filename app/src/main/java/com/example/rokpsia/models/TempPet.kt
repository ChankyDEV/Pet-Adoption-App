package com.example.rokpsia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TempPet(var uid:String,
              var imageUrl:String,
              var name:String,
              var character: PetCharacter?,
              var behavior: PetBehavior?,
              var age:Int?,
              var typ:String,
              var userID:String,
              var health:Float,
              var description:String,
              var vaccinations:MutableList<String>?):Parcelable {

    constructor():this("","","",null,null,null,"","",0.0f,"",null)
}