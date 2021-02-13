package com.example.rokpsia.models



data class User(var uid:String?, var username:String, var password:String,
                var email:String, var accountType:String, var location:String?, var age:String?,
                var sex:String?, var city:String?,
                var voivodeship:String?,var country:String?,var phoneNumber:String){

    constructor():this(null,"","","","",
        "",null,null,null,null,null,"")
}


