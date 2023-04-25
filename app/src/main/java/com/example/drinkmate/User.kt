package com.example.drinkmate

import com.google.firebase.Timestamp

data class User(
    val UID: String = "",
    val deviceToken: String = "",
    val userName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val is_user_location_tracking: Boolean = true,
    val join_date: Timestamp? = null,
    val membership_length: Int? = null,
    val num_recipes_made: Int? = null,
    val num_recipes_viewed: Int? = null,
    val num_recipes_rated: Int? = null,
    val num_barcodes_scanned: Int? = null,
    val num_bars_visited: Int? = null,
    val games_played: Int? = null
)

