package com.example.rokpsia.fragments.loginAndRegister

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rokpsia.R
import com.example.rokpsia.databinding.RegisterTwoBinding
import com.example.rokpsia.utils.Helper
import com.example.rokpsia.viewModel.registerAndLogin.Register2ViewModel
import com.example.rokpsia.viewModel.registerAndLogin.Register2ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.here.sdk.core.Anchor2D
import com.here.sdk.core.GeoCoordinates
import com.here.sdk.core.LanguageCode
import com.here.sdk.gestures.TapListener
import com.here.sdk.mapviewlite.*
import com.here.sdk.search.SearchEngine
import com.here.sdk.search.SearchOptions

class RegisterPart2: Fragment() {

    private lateinit var binding: RegisterTwoBinding
    private lateinit var mapView: MapViewLite
    private val searchEngine = SearchEngine()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: Register2ViewModel by viewModels { Register2ViewModelFactory(Helper.provideRepository(requireContext())) }
    private var mapMarker:MapMarker?=null



    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = RegisterTwoBinding.inflate(inflater,container,false)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())





        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->



                location?.let { setTapGestureHandler(mapView)
                loadMapScene(it.latitude,it.longitude)
                getAddressForCoordinates(it.latitude,it.longitude,searchEngine)
                }
            }

        changeToTwo()

        viewModel.updateSex("woman")

        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->


            if(isChecked) {
                var sex = "woman"
                when (checkedId) {
                    2131361913 -> {
                        sex = "woman"
                    }
                    2131361915 -> {
                        sex = "man"
                    }
                }
                viewModel.updateSex(sex)
            }
        }

        binding.registerBtn.setOnClickListener {

            var isFromEditProfile = arguments?.getBoolean("isFromEditProfile")
            if(isFromEditProfile == true){
                findNavController().navigate(R.id.action_registerP2_to_editProfileFragment)
            }else{
                findNavController().navigate(R.id.action_registerP2_to_mainFragment)
            }


        }
        return binding.root
    }

    //--UNO


    private fun loadMapScene(lat:Double,lng:Double) {
        mapView.mapScene.loadScene(
            MapStyle.NORMAL_DAY
        ) { errorCode ->
            if (errorCode == null) {
                mapView.camera.target = GeoCoordinates(lat,lng)
                mapView.camera.zoomLevel = 14.0
            } else {
                Log.d(TAG, "onLoadScene failed: $errorCode")
            }
        }
    }



    private fun getAddressForCoordinates(lat:Double,lng:Double,searchEngine: SearchEngine) {
        val maxItems = 1
        val reverseFindPlaces = SearchOptions(LanguageCode.EN_GB, maxItems)
        searchEngine.search(
            GeoCoordinates(lat,lng), reverseFindPlaces
        ) { p0, p1 ->
            if(p0==null){
                p1?.let {
                    val place = it[0].address.addressText
                    binding.newPlace.text = place
                    viewModel.updateLocationToUser(place)

                }
            }
        }
    }



    private fun setTapGestureHandler(mapView: MapViewLite) {

        mapView.gestures.tapListener = TapListener { touchPoint ->

            val geoCoordinates = mapView.camera.viewToGeoCoordinates(touchPoint)
            addMarker(geoCoordinates.latitude,geoCoordinates.longitude)


            getAddressForCoordinates(geoCoordinates.latitude,geoCoordinates.longitude,searchEngine)
        }
    }


    private fun addMarker(lat:Double,lng:Double){

        val mapImage = MapImageFactory.fromBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_pin)!!.toBitmap())

        mapMarker?.let {
            mapView.mapScene.removeMapMarker(it)
        }
        mapMarker = MapMarker(GeoCoordinates(lat,lng))

        val mapMarkerImageStyle = MapMarkerImageStyle()
        mapMarkerImageStyle.anchorPoint = Anchor2D(0.5, 1.0)

        mapMarker!!.addImage(mapImage, mapMarkerImageStyle)
        mapView.mapScene.addMapMarker(mapMarker!!)

    }

    private fun View.setAllEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup) children.forEach { child -> child.setAllEnabled(enabled) }
    }

    //---DOS

    private fun changeToTwo(){

        binding.right.setOnClickListener {
            binding.motion.setTransition(R.id.start, R.id.end)
            binding.motion.transitionToEnd()

            binding.motion.setTransitionListener(object:MotionLayout.TransitionListener{
                override fun onTransitionTrigger(
                    p0: MotionLayout?,
                    p1: Int,
                    p2: Boolean,
                    p3: Float
                ) {
                }
                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                    binding.dotsIndicator.setDotSelection(1)
                }

                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                }
            })
        }
    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
        mapView.gestures.tapListener = null
    }

   override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.gestures.tapListener = null
        mapView.onDestroy()
    }


}