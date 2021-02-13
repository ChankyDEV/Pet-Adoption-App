package com.example.rokpsia.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit


class LocationProvider (val context: Context){

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest = LocationRequest().apply {

        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationUpdatePendingIntent: PendingIntent by lazy {

        val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun getStartLocation() {

      if (checkPermission(context)) {

          fusedLocationClient.requestLocationUpdates(locationRequest, locationUpdatePendingIntent)
      }

    }

    fun getStopLocation(){

        fusedLocationClient.removeLocationUpdates(locationUpdatePendingIntent)

    }






    companion object {

        @Volatile private var INSTANCE: LocationProvider? = null

        fun getInstance(context: Context): LocationProvider {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationProvider(context).also { INSTANCE = it }
            }
        }

        fun checkPermission(context: Context): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }

    }



}