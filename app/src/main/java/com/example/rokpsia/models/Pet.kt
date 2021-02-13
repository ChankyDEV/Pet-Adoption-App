package com.example.rokpsia.models

import android.os.Parcelable
import androidx.databinding.BindingAdapter
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "petsFav")
@Parcelize
data class Pet(@PrimaryKey(autoGenerate = false)
                var id:String,
               var userID:String,
               var age:Int?,
               var type:String,
               var name:String,
               @Embedded
               var character: PetCharacter?,
               @Embedded
               var behavior: PetBehavior?,
               var health:Float,
               var description:String,
                var vaccinations:String,
                var voivodeship:String):Parcelable{


    constructor():this("","",null,"","",null,null,0.0F,"","","")


    @BindingAdapter("image")
    fun loadImage(view: SimpleDraweeView, url: String) {
        view.setImageURI(url)
    }
}



