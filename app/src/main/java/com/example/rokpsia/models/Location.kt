package com.example.rokpsia.models

import com.google.gson.annotations.SerializedName

data class Location(@SerializedName("kod")
                    var postalCode:String,
                    @SerializedName("gmina")
                    var community:String,
                    @SerializedName("powiat")
                    var county:String,
                    @SerializedName("wojewodztwo")
                    var voivodeship:String,)