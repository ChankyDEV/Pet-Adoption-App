package com.example.rokpsia.pocztaPolskaAPI


import com.example.rokpsia.models.Location
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path


interface PolishPostService {

    @Headers(
        "Accept: application/json"
    )

    @GET("api/{code}")
    suspend fun getPolishPostService(@Path("code")code:String): List<Location>

    companion object {
        operator fun invoke(): PolishPostService {
            return Retrofit.Builder()
                .baseUrl("http://kodpocztowy.intami.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(PolishPostService::class.java)
        }
    }

}