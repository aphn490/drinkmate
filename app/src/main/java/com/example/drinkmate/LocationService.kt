package com.example.drinkmate

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val db = Firebase.firestore
    private var PERMISSION_ID = 44
    private var locationTimeInterval: Long = 10000
    private var locationFastestInterval: Long = 5000
    private var locationMaxWaitTime: Long = 5000
    private var locationMinDistance: Float = 1.0F //meters

    override fun onCreate() {
        super.onCreate()
        println("STARTINGG SERVICE for LOC ==========================================================================")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = createRequest(locationFastestInterval, locationMinDistance)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Do something with the location data

                    //val location = Location(
                        //latitude = location.latitude,
                        //longitude = location.longitude,
                        //timestamp = location.time
                    //)

                    println(location.latitude)
                    println(location.latitude)
                    println(location.latitude)
                    println(location.latitude)
                    println(location.latitude)
                    println(location.latitude)
                    println(location.latitude)

                    val userCollectionRef = db.collection("UserAccounts")
                    val userRef = userCollectionRef.document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                    val loc = hashMapOf(
                        "location_latitude" to location.latitude,
                        "location_longitude" to location.longitude
                    )
                    userRef.set(loc)
                }
            }
        }

    }

    private fun createRequest(locationFastestInterval: Long, locationMinDistance: Float): LocationRequest = LocationRequest.Builder(locationTimeInterval).apply {
        setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        setWaitForAccurateLocation(false)
        setMinUpdateIntervalMillis(this@LocationService.locationFastestInterval)
        setMaxUpdateDelayMillis(locationMaxWaitTime)
        setMinUpdateDistanceMeters(this@LocationService.locationMinDistance)
        setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
    }.build()

    fun changeRequest(timeInterval: Long, minimalDistance: Float) {
        this.locationTimeInterval = timeInterval
        this.locationMinDistance = minimalDistance
        createRequest(locationFastestInterval, locationMinDistance)
        stopLocationUpdates()
        startLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.flushLocations()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }


}
