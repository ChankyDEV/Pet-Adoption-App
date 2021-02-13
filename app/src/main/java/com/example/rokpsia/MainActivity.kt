package com.example.rokpsia

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rokpsia.databinding.ActivityMainBinding
import com.example.rokpsia.utils.LocationProvider
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView

private const val MY_PERMISSION_CODE = 1
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val navController = findNavController(R.id.fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        //supportActionBar!!.hide()


        navController.addOnDestinationChangedListener { _, destination, _ ->

            when(destination.id){
                R.id.loginFragment -> binding.bottomNavigation.visibility = GONE
                R.id.registerFragment -> binding.bottomNavigation.visibility = GONE
                R.id.registerP2 -> binding.bottomNavigation.visibility = GONE
                else -> binding.bottomNavigation.visibility = VISIBLE
            }
        }


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!LocationProvider.checkPermission(this)) {
            requestLocationPermission()
        }


    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            MY_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location set successfully ", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(this, "Please, set location ", Toast.LENGTH_LONG).show()
        }
    }



}