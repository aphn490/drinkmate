package com.example.drinkmate

data class User(
    val UID: String = "",
    val deviceToken: String = "",
    val userName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val is_user_location_tracking: Boolean = true
)

