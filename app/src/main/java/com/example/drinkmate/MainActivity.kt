package com.example.drinkmate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Context
import android.os.*
import android.util.Log
import com.google.android.gms.location.*
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// Main Activity that will be house multiple fragments and functions
class MainActivity : AppCompatActivity() {
    private var startTime: Long = 0
    private var timer: CountDownTimer? = null
    private var userDocumentRef: DocumentReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityResultContracts.RequestPermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan1 = NotificationChannel(
                "my_channel_id",
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            chan1.description = "My Channel Description" //OPTIONAL
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(chan1)
        }

        println("MAIN START ============================================================")
        //startLocationService()
        val locationUpdateHandler = LocationUpdateHandler(applicationContext, FirebaseAuth.getInstance().currentUser?.uid ?: "")

        // Start receiving location updates
        locationUpdateHandler.startLocationUpdates()

        setContentView(R.layout.activity_main)

        // Navigation Controller that lets you navigate between different fragments through the use
        // of the bottom navigation bar
        val navController = this.findNavController(R.id.nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setupWithNavController(navController)

        //Temp code to furnish existing UserAccount documents with required fields
        val collectionRef = FirebaseFirestore.getInstance().collection("UserAccounts")

        /*
        val joinDate = Timestamp.now()

        collectionRef.get()
            .addOnSuccessListener { documents ->
                // Iterate through all documents and update the field
                for (document in documents) {
                    document.reference.update("join_date", joinDate)
                }
            }

        collectionRef.get()
            .addOnSuccessListener { documents ->
                // Iterate through all documents and update the field
                for (document in documents) {
                    document.reference.update("num_recipes_made", 0)
                }
            }

        collectionRef.get()
            .addOnSuccessListener { documents ->
                // Iterate through all documents and update the field
                for (document in documents) {
                    document.reference.update("num_recipes_viewed", 0)
                }
            }

        collectionRef.get()
            .addOnSuccessListener { documents ->
                // Iterate through all documents and update the field
                for (document in documents) {
                    document.reference.update("num_recipes_rated", 0)
                }
            }

        collectionRef.get()
            .addOnSuccessListener { documents ->
                // Iterate through all documents and update the field
                for (document in documents) {
                    document.reference.update("num_barcodes_scanned", 0)
                }
            }

        collectionRef.get()
            .addOnSuccessListener { documents ->
                // Iterate through all documents and update the field
                for (document in documents) {
                    document.reference.update("num_bars_visited", 0)
                }
            }

        collectionRef.get()
            .addOnSuccessListener { documents ->
                // Iterate through all documents and update the field
                for (document in documents) {
                    document.reference.update("games_played", 0)
                }
            }
         */

        startTime = System.currentTimeMillis()

        FirebaseApp.initializeApp(this)
        val firestore = FirebaseFirestore.getInstance()

        // Get the current user's ID (assuming you have implemented user authentication)
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

        // Get the user's document reference
        userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        /*
        // Start the activity timer to track active screen time
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val activityTime = (System.currentTimeMillis() - startTime) / 1000
                //userDocumentRef?.update("active_time", activityTime)
                userDocumentRef?.update("active_time", FieldValue.increment(activityTime))
            }

            override fun onFinish() {}
        }.start()

         */

        /*
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid ?: return // Return if the user is not authenticated
        val db = Firebase.firestore
        val userDocRef = db.collection("UserAccounts").document(userId)

        val handler = Handler(Looper.getMainLooper())
        val task = object : Runnable {
            override fun run() {
                val elapsedTimeHours = (SystemClock.elapsedRealtime() - startTime) / (1000 * 60 * 60) // Convert milliseconds to hours
                userDocRef.update("membership_length", FieldValue.increment(elapsedTimeHours))
                    .addOnSuccessListener {
                        Log.d("MyApp", "Membership length updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("MyApp", "Error updating membership length", e)
                    }
                handler.postDelayed(this, 5000)
            }
        }
        handler.postDelayed(task, 5000)
         */
    }

    //Override onResume to reset start time to current time
    override fun onResume() {
        super.onResume()
        startTime = System.currentTimeMillis()
    }

    //Override onPause to save elapsed screen time
    override fun onPause() {
        super.onPause()
        timer?.cancel()
        val activityTime = (System.currentTimeMillis() - startTime) / 1000
        //userDocumentRef?.update("membership_length", activityTime)
        userDocumentRef?.update("active_time", FieldValue.increment(activityTime))
    }

    //Override onDestroy to save elapsed screen time
    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        val activityTime = (System.currentTimeMillis() - startTime) / 1000
        //userDocumentRef?.update("membership_length", activityTime)
        userDocumentRef?.update("active_time", FieldValue.increment(activityTime))
    }

}