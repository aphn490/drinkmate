package com.example.drinkmate

//BarEntity represents kotlin objects converted from JSON object to be retrieved from an OKHTTP request for local bars with Yelp API
class BarEntity {
    //Bar Object
    data class Bar(
        //Yelp's internal id for their businesses
        var id: String,
        var name: String,
        var phone: String,
        //Price from $-$$$$
        var price: String,
        //Rating from 1-5
        var rating: Double,
        var review_count: Int,
        var location: BarLocation,
        var coordinates: BarLatLong,
        //Distance in meters from user local
        var distance: Double,
        var is_closed: Boolean,
        var url: String,
        var image_url: String,
    )
    //Class Implementing the coordinates for a Bar Object
    data class BarLatLong (
        var latitude: Double,
        var longitude: Double,
    )
    //Class Implementing the location for a Bar Object
    data class BarLocation (
        var address1: String,
        var city: String,
        var zip_code: Int,
        var state: String,
        var country: String
    )
}