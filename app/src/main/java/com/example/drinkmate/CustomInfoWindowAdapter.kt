package com.example.drinkmate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

//CustomInfoWindowAdapter Class to create an implementation of the InfoWindowAdapter interface to create a custom info window for Google Map markers
class CustomInfoWindowAdapter(private val context: Context): GoogleMap.InfoWindowAdapter  {
    //Associate this class with the custom_info_window XML layout created to override the default market info window, take the XML layout and parse it to the view
    private val infoWindow: View = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

    private fun renderWindowText(marker: Marker, view: View) {
        //Retrieve the marker tag (associated data with a marker)
        val bar = marker.tag as BarEntity.Bar

        //Set image for the custom info window with image_url from Bar Object (retrieved from Yelp API)
        val barImageView = infoWindow.findViewById<ImageView>(R.id.barImageView)
        Picasso.get().load(bar.image_url).into(barImageView)
        Picasso.get().load(bar.image_url).into(barImageView)

        //Set title for the custom info window with bar name from Bar Object (retrieved from Yelp API)
        val nameTextView = infoWindow.findViewById<TextView>(R.id.title)
        nameTextView.text = bar.name

        //Set snippet for the custom info window with bar address, phone, and cost from Bar Object (retrieved from Yelp API)
        val addressTextView = infoWindow.findViewById<TextView>(R.id.snippet)

        var address = "${bar.location.address1}, ${bar.location.city}, ${bar.location.state} ${bar.location.zip_code}"
        var phone = bar.phone
        var cost = bar.price

        //Yelp API can return empty and null strings, so replacing with N/A for uniformity
        if(bar.phone == "" || bar.phone == null) {
            phone = "N/A"
        }
        if(bar.price == "" || bar.price == null) {
            cost = "N/A"
        }

        val snippet = "Address: ${address}\nPhone: ${phone}\nRating: ${bar.rating}\nCost: ${cost}"
        addressTextView.text = snippet
    }

    //Implement getInfoContents method for interface
    override fun getInfoContents(marker: Marker): View? {
        renderWindowText(marker, infoWindow)
        return infoWindow
    }

    //Implement getInfoWindow method for interface
    override fun getInfoWindow(marker: Marker): View? {
        renderWindowText(marker, infoWindow)
        return infoWindow
    }
}