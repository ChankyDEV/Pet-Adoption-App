package com.example.rokpsia.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationResult
import java.util.*

class LocationUpdatesBroadcastReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("1", "onReceive() context:$context, intent:$intent")

        if (intent.action == ACTION_PROCESS_UPDATES) {
            LocationResult.extractResult(intent)?.let { locationResult ->
                val locations = locationResult.locations.map { location ->
                    location.latitude
                    location.longitude
                    Date(location.time)
                }

                if (locations.isNotEmpty()) {
                   //ADD TO DATABASE
                }
            }
        }
    }


    companion object {
        const val ACTION_PROCESS_UPDATES =
            "com.example.rokpsia.utils.action." +
                    "PROCESS_UPDATES"
    }


}