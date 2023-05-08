package com.example.drinkmate

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

import com.example.drinkmate.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

//MapActivity class represents the activity implementation for the bar map
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    //Global variables to save values that would otherwise require coroutines or a callback interface
    private var requestUrl: String = ""
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var loc: Location? = null
    private var sentLocLat: Double = 0.0
    private var sentLocLong: Double = 0.0
    private var isGPS: Boolean = false
    private var PERMISSION_ID = 44
    private var barObjArrayListGlobal: ArrayList<BarEntity.Bar>? = null
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {
            //println(it.name)
        }
        binding = ActivityMapsBinding.inflate(layoutInflater)
        getGPSLocation(this)
        setContentView(binding.root)


        //================================================
        //Use AlertDialog to create and then inflate a dialog popup for the bar map filter settings page to the view
        //Initialize the dialogView to the bar_map_filters_popup XML layout
        //================================================
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.bar_map_filters_popup, null)
        dialogBuilder.setView(dialogView)

        //================================================
        //Create a listener for editText_location, the user-input text-field for city, state, zipcode
        //================================================
        val editTextLocation = dialogView.findViewById<EditText>(R.id.editText_location)
        var locationStr = ""
        editTextLocation.addTextChangedListener(object : TextWatcher {
            //Save the user input location as a string after the user inputs the text
            override fun afterTextChanged(s: Editable) {
                locationStr = editTextLocation.text.toString()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        //================================================
        //Create a listener for switch_location, the switch widget for whether the user wants to use their device GPS location
        //If on, disable the text-field for location and delete any inputted text, set useGPSLocation to true
        //If off, enable the text-field for location, set useGPSLocation to false
        //================================================
        val switchLocation = dialogView.findViewById<Switch>(R.id.switch_location)
        var useGPSLocation = false

        switchLocation.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    editTextLocation.isFocusable = false
                    editTextLocation.isFocusableInTouchMode = false
                    editTextLocation.isEnabled = false
                    editTextLocation.text.clear()
                    editTextLocation.alpha = 0.5f
                    useGPSLocation = true
                    isGPS = true
                }
                else {
                    editTextLocation.isFocusable = true
                    editTextLocation.isFocusableInTouchMode = true
                    editTextLocation.isEnabled = true
                    editTextLocation.alpha = 1f
                    useGPSLocation = false
                    isGPS = false
                }
            }
        })

        //================================================
        //Create a listener for seekBar_radius, the seekbar for the radius bar map filter settings
        //Action: Update textViewRadius to display the current radius value
        //================================================
        val textViewRadius = dialogView.findViewById<TextView>(R.id.textView_radius)
        textViewRadius.text = "Radius: 1/30 miles"

        val seekBarRadius = dialogView.findViewById<SeekBar>(R.id.seekBar_radius)
        var radiusValue = 0
        seekBarRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromuser: Boolean) {
                radiusValue = progress
                textViewRadius.text = "Radius: ${radiusValue}/30 miles"
                if (radiusValue == 0) {
                    textViewRadius.text = "Radius: 1/30 miles"
                }
            }
            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                println("onStartTrackingTouch")
            }
            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                println("onStopTrackingTouch")
            }
        })

        //================================================
        //Create a listener for seekBar_rating, the seekbar for the rating bar map filter settings
        //Action: Update textViewRating to display the current rating value
        //================================================
        val textViewRating = dialogView.findViewById<TextView>(R.id.textView_rating)
        textViewRating.text = "Rating: Any"

        val seekBarRating = dialogView.findViewById<SeekBar>(R.id.seekBar_rating)
        var ratingValue = 0
        seekBarRating.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromuser: Boolean) {
                ratingValue = progress
                var realRatingValue = 0
                if(ratingValue == 0) {
                    realRatingValue = 0
                } else if (ratingValue == 1) {
                    realRatingValue = 2
                } else if (ratingValue == 2) {
                    realRatingValue = 3
                } else if (ratingValue == 3) {
                    realRatingValue = 4
                } else if (ratingValue == 4) {
                    realRatingValue = 5
                } else if (ratingValue == 5) {
                    realRatingValue = 5
                }
                if (realRatingValue == 0) {
                    textViewRating.text = "Rating: Any"
                } else {
                    textViewRating.text = "Rating: ${realRatingValue}/5 stars"
                }
            }
            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                println("onStartTrackingTouch")
            }
            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                println("onStopTrackingTouch")
            }
        })

        //================================================
        //Create a listener for seekBar_cost, the seekbar for the cost bar map filter settings
        //Action: Update textViewCost to display the current cost value
        //================================================
        val textViewCost = dialogView.findViewById<TextView>(R.id.textView_cost)
        textViewCost.text = "Cost: Any"

        val seekBarCost = dialogView.findViewById<SeekBar>(R.id.seekBar_cost)
        var costValue = 0
        seekBarCost.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromuser: Boolean) {
                costValue = progress
                if(costValue == 0) {
                    textViewCost.text = "Cost: Any"
                } else if (costValue == 1) {
                    textViewCost.text = "Cost: $"
                } else if (costValue == 2) {
                    textViewCost.text = "Cost: $$"
                } else if (costValue == 3) {
                    textViewCost.text = "Cost: $$$"
                } else if (costValue == 4) {
                    textViewCost.text = "Cost: $$$$"
                }
            }
            override fun onStartTrackingTouch(seekbar: SeekBar?) {
                println("onStartTrackingTouch")
            }
            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                println("onStopTrackingTouch")
            }
        })

        //Build the alertdialog for the filter settings page
        val alertDialog = dialogBuilder.create()
        //Inflate the dialog popup to the view
        alertDialog.show()

        //================================================
        //Create a listener for confirm_button, the button to confirm the user's bar map filter settings
        //Action: Close the popup dialog for filter settings, updates the map based on settings
        //================================================
        val confirmButton = dialogView.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener {
            alertDialog.dismiss()

            requestUrl = generateString(useGPSLocation, locationStr, radiusValue, ratingValue, costValue)
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    //Start a new thread to run doNetworkOperation() in the background instead of as an asynchronous task to prevent main thread blockage
    //Allows us to perform network operations in a more thread-safe manner and avoid sync issues/race conditions
    private fun NetworkOp() {
        Thread(Runnable {
            barObjArrayListGlobal = doNetworkOp()
        }).start()
    }

    //Thread task: execute requesting() method to request results from Yelp API
    //Return arraylist of bar objects
    private fun doNetworkOp(): ArrayList<BarEntity.Bar> {
        var barObjs = requesting()
        return barObjs
    }

    //Use extracted value: arraylist of bar objects from requesting()
    //Waits until resource is free (latch unlocks) for thread, makes sure result is delivered
    private fun useResult() {
        while (barObjArrayListGlobal == null) {
            Thread.sleep(100)
        }
        val finalBarObjArrayList = barObjArrayListGlobal
        println(finalBarObjArrayList)
    }


    @SuppressLint("MissingPermission")
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener(this)
        //Customize the UI Settings for Google Map
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

        //Set google maps to use the custom info window
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        mMap.uiSettings.isZoomControlsEnabled = true;
        mMap.uiSettings.isZoomGesturesEnabled = true;
        mMap.uiSettings.isMapToolbarEnabled = true;
        mMap.uiSettings.isMapToolbarEnabled = true;
        mMap.uiSettings.isCompassEnabled = true;
        mMap.uiSettings.isMyLocationButtonEnabled = true;
        mMap.uiSettings.isRotateGesturesEnabled = true;
        mMap.uiSettings.isScrollGesturesEnabled = true;
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true;
        mMap.uiSettings.isMyLocationButtonEnabled = true;
        if (!checkLocationPermission(this)) {
            requestLocationPermissions(this)
        }
        else {
            mMap.isMyLocationEnabled = true
        }

        //Move google maps camera to area in question
        if (getIsGps()) {
            var tempLoc = getUserLocation()
            if (tempLoc != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(tempLoc.latitude, tempLoc.longitude)))
            }
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(getSentLatLocation(), getSentLongLocation())))
        }
        //Zoom into area in question
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13f))
        //Set a listener for clicking on a google maps marker
        mMap.setOnMarkerClickListener(this)

        //Avoid crash on android.os.NetworkOnMainThreadException errors, error thrown when app tries to perform network operations in main thread
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //Request for arraylist of bar objects
        //Performs a network operation to YelpAPI so create a new thread to avoid running on main thread
        NetworkOp()
        useResult()
        val barObjArrayList = getBarObjArrayList()

        //Create a google map marker for every bar object
        if (barObjArrayList != null) {
            for(barObj in barObjArrayList) {
                //println("barobj: ${barObj}")
                var marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(barObj.coordinates.latitude, barObj.coordinates.longitude))
                        .icon(vectorToBitmap(this, R.drawable.ic_baseline_local_bar_24))
                )
                marker?.tag = barObj
            }
        }
    }

    // Overrides the function that listens to user clicking on the info window that pops up
    override fun onInfoWindowClick(marker: Marker) {
        // Retrieves the user ID from firebase's authentication
        val currentuid = FirebaseAuth.getInstance().currentUser?.uid
        // Sets the marker information to a bar entity object
        val bar = marker.tag as BarEntity.Bar
        // Creates a hashmap of bar information that consists of the bar's coordinates, name, and url image.
        val locBar = hashMapOf (
            "name" to bar.name,
            "lat" to bar.coordinates.latitude,
            "long" to bar.coordinates.longitude,
            "image" to bar.image_url
        )

        // Brings up the document under the FavoriteBars collection that has the user's id from firebase
        val namesRef = db.collection("FavoriteBars").document(currentuid.toString())
        namesRef.get().addOnCompleteListener {
                task ->
            //Once the task is completed, it'll undergo the following code
            if (task.isSuccessful) {
                val doc = task.result
                // If bar 1 doesn't exist yet, it will update the document with the bar details under
                // the name "bar1"
                if (doc.get("bar1") == null) {
                    val favBar = hashMapOf (
                        "bar1" to locBar
                    )
                    db.collection("FavoriteBars").document(currentuid.toString()).set(favBar, SetOptions.merge())
                    Toast.makeText(this, "Added " + bar.name + " as a favorite bar", Toast.LENGTH_SHORT).show()
                    // If bar 2 doesn't exist yet, it will update the document with the bar details under
                    // the name "bar2"
                } else if (doc.get("bar2") == null) {
                    val favBar = hashMapOf (
                        "bar2" to locBar
                    )
                    db.collection("FavoriteBars").document(currentuid.toString()).set(favBar, SetOptions.merge())
                    Toast.makeText(this, "Added " + bar.name + " as a favorite bar", Toast.LENGTH_SHORT).show()
                    // If bar 3 doesn't exist yet, it will update the document with the bar details under
                    // the name "bar3"
                } else if (doc.get("bar3") == null) {
                    val favBar = hashMapOf (
                        "bar3" to locBar
                    )
                    db.collection("FavoriteBars").document(currentuid.toString()).set(favBar, SetOptions.merge())
                    Toast.makeText(this, "Added " + bar.name + " as a favorite bar", Toast.LENGTH_SHORT).show()
                    // If bar 4 doesn't exist yet, it will update the document with the bar details under
                    // the name "bar4"
                } else if (doc.get("bar4") == null) {
                    val favBar = hashMapOf (
                        "bar4" to locBar
                    )
                    db.collection("FavoriteBars").document(currentuid.toString()).set(favBar, SetOptions.merge())
                    Toast.makeText(this, "Added " + bar.name + " as a favorite bar", Toast.LENGTH_SHORT).show()
                    // If bar 5 doesn't exist yet, it will update the document with the bar details under
                    // the name "bar5"
                } else if (doc.get("bar5") == null) {
                    val favBar = hashMapOf (
                        "bar5" to locBar
                    )
                    db.collection("FavoriteBars").document(currentuid.toString()).set(favBar, SetOptions.merge())
                    Toast.makeText(this, "Added " + bar.name + " as a favorite bar", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Max Favorite Bars reached. Delete in more settings page.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(TAG, "get failed with ", task.exception)
            }
        }
    }


    //Get barObjArrayListGlobal, arraylist of bar objects
    fun getBarObjArrayList(): ArrayList<BarEntity.Bar>? {
        return barObjArrayListGlobal
    }

    //Request Android location permissions
    fun requestLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    //Check Android location permissions
    private fun checkLocationPermission(context: Context): Boolean {
        println("gpsLocation....")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    //Get the current GPS location of the user's device
    @SuppressLint("MissingPermission")
    private fun getGPSLocation(context: Context) {
        if (!checkLocationPermission(this)) {
            requestLocationPermissions(context as Activity)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    //println("Location= " + "Latitude: ${location.latitude}+ Longitude: ${location.longitude}")
                    loc = location
                }
            }
    }

    //Get barObjArrayListGlobal, arraylist of bar objects
    fun getUrlRequestString(): String {
        return requestUrl
    }

    //Get loc, location of user device
    fun getUserLocation(): Location? {
        return loc
    }

    //Get sentLocLat, latitude coordinate of user inputted address
    fun getSentLatLocation(): Double {
        return sentLocLat
    }

    //Get sentLocLong, longitude coordinate of user inputted address
    fun getSentLongLocation(): Double {
        return sentLocLong
    }

    //Get isGPS, boolean as to whetheer wants to use gps or inputed location
    fun getIsGps(): Boolean {
        return isGPS
    }

    //Generate and return URL string for making a search query to YelpAPI
    fun generateString(useGPSLocation: Boolean, locationStr: String, radiusValue: Int, ratingValue: Int, costValue: Int): String {
        //Use Uri Builder as a convient way to parse contents of a Url instance
        val ubuilder = Uri.Builder()
        ubuilder.scheme("https")
            .authority("api.yelp.com")
            .appendPath("v3")
            .appendPath("businesses")
            .appendPath("search")
            .appendQueryParameter("term", "bars")

        //If user enables gps location then use gps location, else use the user inputted location
        if (useGPSLocation) {
            var userLoc = getUserLocation()
            if (userLoc != null) {
                ubuilder.appendQueryParameter("latitude", userLoc.latitude.toString())
                ubuilder.appendQueryParameter("longitude", userLoc.longitude.toString())
                var tempLoc = Location("")
            }
        } else {
            ubuilder.appendQueryParameter("location", locationStr)
            val geocoder = Geocoder(this)
            val coords = geocoder.getFromLocationName(locationStr, 1)
            val latitude = coords!![0].latitude
            val longitude = coords!![0].longitude

            sentLocLat = latitude
            sentLocLong = longitude
        }

        //If user doesn't touch radius filter settings then default to 1 mile, else use user supplied radius filter setting
        if (radiusValue != 0) {
            var meterRadius = radiusValue * 1609.344
            ubuilder.appendQueryParameter("radius", meterRadius.roundToInt().toString())
        } else {
            ubuilder.appendQueryParameter("radius", "1610")
        }

        //If user doesn't touch rating filter settings then default to $, else use user supplied rating filter setting
        if (ratingValue != 0) {
        } else if (ratingValue == 4) {
            ubuilder.appendQueryParameter("sort_by", "rating")
        }

        //If user doesn't touch cost filter settings then default to 1, else use user supplied cost filter setting
        if (costValue != 0) {
            if (costValue == 1) {
                ubuilder.appendQueryParameter("price", "1")
            }
            else if (costValue == 2) {
                ubuilder.appendQueryParameter("price", "2")
            }
            else if (costValue == 3) {
                ubuilder.appendQueryParameter("price", "3")
            }
            else if (costValue == 4) {
                ubuilder.appendQueryParameter("price", "4")
            } else {
                ubuilder.appendQueryParameter("price", "1")
            }
        } else {
        }

        //Build URL string
        val urlStr = ubuilder.build().toString()
        return urlStr
    }

    //Make a search query to YelpAPI using an OKHTTP Client
    //Return an arraylist of bar objects
    fun requesting(): ArrayList<BarEntity.Bar> {
        //Initialize OkHttpClient, interfaces with java socket and implements a http url connection
        //Specify a max timeout length so we have enough time to fetch all bar objects
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        //Construct a request to the YelpAPI with a search query URL string
        val request = Request.Builder()
            .url(getUrlRequestString())
            //.url("https://api.yelp.com/v3/businesses/search?term=bar&latitude=${lat}&longitude=${long}&limit=30")
            .get()
            .addHeader("Authorization", "Bearer VVV6HapGNrAgysyJ1cDFohZ7ztCZCiAVvmTIV_nUY_foloU4sYsmsg65VOBGu2NzqqvdYapFq4pHJxNHF0Lb3NBkcdo3PTsCutDOkJYYAwv-RIQAca8V7Ji6hZbgY3Yx")
            .build()
        //Execute the request to the YelpAPI
        val response = client.newCall(request).execute()
        //Save the reponse from the request as a Json String
        //peekBody(Long.MAX_VALUE) used to get the whole response body and avoid buffer consumption
        var resB = response.peekBody(Long.MAX_VALUE).string()

        //Convert the Json String into a Json Object
        var jsonObj = JSONObject(resB)
        //Convert the Json Object into a Json Array
        var jsonArray = jsonObj.getJSONArray("businesses")
        //Initialize Google Gson to serialize json objects into kotlin objects
        var gson = Gson()
        //For every json object in the json array, convert json object to a bar object and append that to an array of bar objects
        var barObjArrayList = ArrayList<BarEntity.Bar>()
        for (i in 0 until jsonArray.length()) {
            val barJsonObj = jsonArray.getJSONObject(i)
            val barJsonObjStr = barJsonObj.toString()
            println(barJsonObjStr)
            val barObj = gson.fromJson(barJsonObjStr, BarEntity.Bar::class.java)
            barObjArrayList.add(barObj)
            println(barObj.coordinates.latitude)
            println(barObj.coordinates.longitude)
        }
        return barObjArrayList
    }

    //Convert vector drawable to bitmap icon for google map marker (INT -> drawable -> bitmap? ->  BitmapDescriptor?)
    //Used for Android Studio's drawable vector assets
    fun vectorToBitmap(context: Context, vectorId: Int): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(this, vectorId)
        val bitmapDesc = (drawable?.toBitmap())?.let { BitmapDescriptorFactory.fromBitmap(it) }
        return bitmapDesc
    }

    //Implement onMarkerClick for GoogleMap.OnMarkerClickListener interface, actions for when a marker is clicked by user.
    // Return a boolean- false means event not consumed so default behavior is camera moves to center at marker and marker info window to open
    override fun onMarkerClick(marker: Marker): Boolean {
        // Retrieve the data from the marker.
        val markerTag = marker.tag as? Int

        //Temp fix for infoWindow image not showing until after 2nd click
        marker.hideInfoWindow()
        marker.showInfoWindow()
        marker.showInfoWindow()
        return false
    }

}