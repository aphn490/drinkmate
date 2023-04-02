package com.example.drinkmate

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
//import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.ContentValues.TAG

//LocationUpdateHandler handles location fetching for user device
class LocationUpdateHandler(private val context: Context, private val uid: String) {
    // Initialize variables to control location updates
    private var locationTimeInterval: Long = 5000
    private var locationFastestInterval: Long = 5000
    private var locationMaxWaitTime: Long = 5000
    private var locationMinDistance: Float = 1.0F //meters

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            for (location in locationResult.locations) {
                uploadLocation(location)
            }
        }
    }

    //Start location updates, config settings for location request
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(locationTimeInterval).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            setWaitForAccurateLocation(false)
            setMinUpdateIntervalMillis(locationFastestInterval)
            setMaxUpdateDelayMillis(locationMaxWaitTime)
            //setMinUpdateDistanceMeters(locationMinDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        }.build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    //Stop location updatees
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    //Update user data fieldss for location
    private fun uploadLocation(location: Location) {
        val db = FirebaseFirestore.getInstance()
        val userAccountRef = db.collection("UserAccounts").document(FirebaseAuth.getInstance().currentUser?.uid ?: "")

        val data = mapOf(
            "location_latitude" to location.latitude,
            "location_longitude" to location.longitude,
            "location_lastUpdated" to location.time,
        )

        userAccountRef.update(data)
            .addOnSuccessListener {
                // Location data uploaded successfully
                Log.d(TAG, "Location updated successfully")
            }
            .addOnFailureListener { e ->
                // Error uploading location data
                Log.w(TAG, "Error updating location", e)
            }
    }
}
