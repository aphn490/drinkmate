package com.example.drinkmate

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap
//=============================================================================================
//FriendMapFragment is responsible for implementing the functions for the friend map feature
//=============================================================================================
class FriendMapFragment : Fragment(), OnMapReadyCallback {
    // Initialize variables to control map and map settings
    private lateinit var mMap: GoogleMap
    private lateinit var mapFrag : SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationUpdateHandler: LocationUpdateHandler
    private val friendMarkers = HashMap<String, Marker>()
    private var canView = true

    private val TIMER_INTERVAL = 60 * 1000L // 1 minute
    private var timer: Timer? = null

    val db = Firebase.firestore

    private var mapView: MapView? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirestoreRecyclerAdapter<User, FriendMapFragment.FriendViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize the LocationUpdateHandler
        locationUpdateHandler = LocationUpdateHandler(requireContext(), FirebaseAuth.getInstance().currentUser?.uid ?: "")
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend_map, container, false)

        val backButton = view.findViewById<ImageButton>(R.id.back_button_toSocial)

        //Create a click listener for the map button and implement action, navigates to bar map when clicked
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_friendMapFragment_to_social)
        }

        val settingsButton = view.findViewById<ImageButton>(R.id.friend_map_settings_button)

        //Click listener for settings button, inflates a dialog popup for the friend map settings menu
        settingsButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.friend_map_settings_popup, null)
            dialogBuilder.setView(dialogView)

            val showUserLocationSwitch = dialogView.findViewById<Switch>(R.id.switch_friendMapSettings_userLocation)

            //Listener for switch for whether user wants to enable or disable location tracking
            showUserLocationSwitch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                    if (isChecked) {
                        showUserLocationSwitch.text = "Enabled"
                        showUserLocationSwitch.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
                        setUserLocationTracking(true)
                        val message = "Your location is tracking!"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        showUserLocationSwitch.text = "Disabled"
                        showUserLocationSwitch.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        setUserLocationTracking(false)
                        val message = "Your location has stopped tracking!"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })


            recyclerView = dialogView.findViewById(R.id.recyclerview_friendMapSettings_friendList)

            val query = FirebaseFirestore.getInstance()
                .collection("UserAccounts")
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .collection("friends")

            //Adapter options set to the query made to the list of friends
            val options = FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .build()

            //Initialize RecyclerView RecyclerView
            adapter = object : FirestoreRecyclerAdapter<User, FriendMapFragment.FriendViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendMapFragment.FriendViewHolder {
                    val friendsListView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_friendmapsettings, parent, false)
                    return FriendViewHolder(friendsListView)
                }

                override fun onBindViewHolder(holder: FriendMapFragment.FriendViewHolder, position: Int, model: User) {
                    holder.bind(model)
                }
            }

            adapter.startListening()

            recyclerView.adapter = adapter

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            // set up any buttons or other UI elements in dialog as needed

            //Confirm friend map settings
            val confirmButton = dialogView.findViewById<Button>(R.id.button_friendMapSettings_confirm)
            confirmButton.setOnClickListener {
                updateFriendMarkers()
                alertDialog.dismiss()
                adapter.stopListening()
            }

            //Cancel friend map settings
            val cancelButton = dialogView.findViewById<Button>(R.id.button_friendMapSettings_cancel)
            cancelButton.setOnClickListener {
                alertDialog.dismiss()
                adapter.stopListening()
            }
        }
        mapFrag = childFragmentManager.findFragmentById(R.id.friend_map_view_container) as SupportMapFragment
        mapFrag.getMapAsync(this)

        // Return the inflated layout as a View object
        return view
    }

    //onStart method for after view activity is active
    override fun onStart() {
        super.onStart()
    }

    //onStop method for after view activity ceases
    override fun onStop() {
        super.onStop()

    }

    //FriendViewholder class to implement the view holder object for holding the friend elements in the friends list
    private inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profileImageView: ImageView = view.findViewById(R.id.friendmapcard_profile_image)
        private val friendNameTextView: TextView = view.findViewById(R.id.friendmapcard_name)
        private val checkBoxButton: CheckBox = view.findViewById(R.id.friendmapcard_checkBox1)
        private var isFriendSelected = true

        init {
            view.setOnClickListener {
                // Perform the action you want when the item is clicked
                // For example, increase the opacity of the cardview or un-grey the color
                // Update the Firebase value associated with the clicked friend

                if (isFriendSelected) {
                    view.alpha = 0.2f
                    val message = "Off!"
                    checkBoxButton.isChecked = false
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    updateFriendMarkers()
                    mMap.clear()
                } else {
                    view.alpha = 1.0f
                    val message = "On!"
                    checkBoxButton.isChecked = true
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    updateFriendMarkers()
                    mMap.clear()
                }
                isFriendSelected = !isFriendSelected



            }
        }

        // Bind the data to the views in the item layout
        fun bind(user: User) {
            /*
            Glide.with(itemView.context)
                .load(user.profileImageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(profileImageView)
             */

            val fsdb = FirebaseFirestore.getInstance()
            val usersCollectionRef = fsdb.collection("UserAccounts")
            val userRef = usersCollectionRef.document(user.UID)
            userRef.get().addOnSuccessListener { documentSnapshot ->
                val field = documentSnapshot.getString("userName")
                friendNameTextView.text = field
            }.addOnFailureListener { exception ->
                // handle the exception here
            }

            checkBoxButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Checkbox is checked
                    setCanViewLocation(user, true)

                } else {
                    // Checkbox is not checked
                    setCanViewLocation(user, false)
                }
            }
        }
    }

    //Set whether user wants to view friend marker on map
    fun setCanViewLocation(user: User, bool: Boolean) {
        val db = FirebaseFirestore.getInstance()
        val friendId = user.UID
        val userAccountRef = db.collection("UserAccounts").document(FirebaseAuth.getInstance().currentUser?.uid ?: "").collection("friends").document(friendId)

        val data = mapOf(
            "can_view_location" to bool
        )
        userAccountRef.update(data)
    }

    //Enable or disable tracking for user
    fun setUserLocationTracking(bool: Boolean) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val userAccountRef = db.collection("UserAccounts").document(userId)

        val data = mapOf(
            "is_user_location_tracking" to bool
        )
        userAccountRef.update(data)
    }

    //Implement customization for map once ready
    override fun onMapReady(map: GoogleMap) {
        mMap = map


        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
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

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val docRef = db.collection("UserAccounts").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val lat = document.getDouble("location_latitude")
                    val long = document.getDouble("location_longitude")
                    if (lat != null && long != null) {
                        val userLoc = LatLng(lat, long)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc))
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        //Zoom into area in question
        mMap.moveCamera(CameraUpdateFactory.zoomTo(8f))

        // Add your map customization
        updateFriendMarkers()
    }

    //Get whether the user has friend enabled or disabled for displaying map marker
    private fun getCanView(currentUserId: String, friendId: String) {
        val fsdb = FirebaseFirestore.getInstance()
        val usersCollectionRef1 =
            fsdb.collection("UserAccounts").document(currentUserId).collection("friends")
                .document(friendId)
        usersCollectionRef1.get().addOnSuccessListener { friendDocument1 ->
            canView = friendDocument1.getBoolean("can_view_location")!!
        }
    }

    //Update friend markers on the map based on friends in friends lists, whether friend has tracking enabled, and whether user has friend selected to display marker in settings
    private fun updateFriendMarkers() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var canView = true
        if (currentUser != null) {
            val currentUserId = currentUser.uid ?: ""
            val fsdb = FirebaseFirestore.getInstance()
            val usersCollectionRef = fsdb.collection("UserAccounts")
            val userRef = usersCollectionRef.document(currentUserId)
            userRef.collection("friends").addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed", e)
                    return@addSnapshotListener
                }

                for (document in snapshot?.documents!!) {
                    val friendId = document.id
                    //val usersCollectionRef1 = fsdb.collection("UserAccounts").document(currentUserId).collection("friends").document(friendId)
                        //usersCollectionRef1.get().addOnSuccessListener { friendDocument1 ->
                            //canView = friendDocument1.getBoolean("can_view_location")!!
                        //}

                    getCanView(currentUserId, friendId)

                    val friendRef = FirebaseFirestore.getInstance().collection("UserAccounts").document(friendId)
                    friendRef.get().addOnSuccessListener { friendDocument ->
                        if (friendDocument.exists()) {
                            val isTrack = friendDocument.getBoolean("is_user_location_tracking")
                            val friendLatitude = friendDocument.getDouble("location_latitude")
                            val friendLongitude = friendDocument.getDouble("location_longitude")
                            val name = friendDocument.getString("userName")
                            if (friendLatitude != null && friendLongitude != null) {
                                val friendLatLng = LatLng(friendLatitude, friendLongitude)
                                if (isTrack == true && canView == true) {
                                    val marker = mMap.addMarker(MarkerOptions()
                                        .position(friendLatLng)
                                        .title(name)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.person32))

                                    )
                                    marker?.showInfoWindow()
                                    if (marker != null) {
                                        marker.tag = friendId
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Convert vector drawable to bitmap icon for google map marker (INT -> drawable -> bitmap? ->  BitmapDescriptor?)
    //Used for Android Studio's drawable vector assets
    fun vectorToBitmap(context: Context, vectorId: Int): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(requireContext(), vectorId)
        val bitmapDesc = (drawable?.toBitmap())?.let { BitmapDescriptorFactory.fromBitmap(it) }
        return bitmapDesc
    }

    //Handle looping function to periodically update friend location markers
    private val handler = Handler(Looper.getMainLooper())
    private val updateMarkersTask = object : Runnable {
        override fun run() {
            mMap.clear()
            updateFriendMarkers()
            handler.postDelayed(this, 5000)
        }
    }

    //Start the loop handler
    private fun startTimer() {
        handler.postDelayed(updateMarkersTask, 0)
    }

    //Stop the loop handler
    private fun stopTimer() {
        handler.removeCallbacks(updateMarkersTask)
    }

    //Resume functions when the app is visible on screen
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        startTimer()
    }

    //Pause functions when the app is not visible on screen
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        stopTimer()
    }

    //Start tracking location
    private fun startLocationUpdates() {
        locationUpdateHandler.startLocationUpdates()
    }

    //Stop tracking location
    private fun stopLocationUpdates() {
        locationUpdateHandler.stopLocationUpdates()
    }
}