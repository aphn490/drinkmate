package com.example.drinkmate

import android.location.Location

//Data class for location object
data class Location(
    val latitude: Location,
    val longitude: Double,
    val timestamp: Long
)