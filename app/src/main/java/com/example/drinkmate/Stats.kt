package com.example.drinkmate

import android.content.ContentValues.TAG
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import androidx.navigation.Navigation

//=============================================================
//Fragment that implements methods for the statistics fragment
//=============================================================
class Stats : Fragment() {
    lateinit var fs: FirebaseFirestore
    lateinit var userID: String
    lateinit var userName_textView: TextView
    lateinit var joinDate_textView: TextView
    lateinit var memberLength_textView: TextView

    lateinit var activeTime_textView: TextView
    lateinit var recipesCreated_textView: TextView
    lateinit var recipesView_textView: TextView
    lateinit var recipesRated_textView: TextView
    lateinit var friends_textView: TextView
    lateinit var groups_textView: TextView
    lateinit var barcodesScanned_textView: TextView
    lateinit var barsVisited_textView: TextView
    lateinit var gamesPlayed_textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //println("OnCreateView==============================================================")
        //Log.d("MyTag", "This is a debug message")
        //textView = rootView.findViewById(R.id.textView)
        val rootView = inflater.inflate(R.layout.fragment_stats, container, false)

        // Get the TextView from the layout

        fs = FirebaseFirestore.getInstance()
        userID = FirebaseAuth.getInstance().currentUser?.uid?: ""

        //Find and set IDs for views in the stats fragment
        //Views for the profile card
        userName_textView = rootView.findViewById<TextView>(R.id.user_name_stats)
        joinDate_textView = rootView.findViewById<TextView>(R.id.join_date_stats)
        memberLength_textView = rootView.findViewById<TextView>(R.id.membership_duration_stats)

        //Views for the other stats
        activeTime_textView = rootView.findViewById<TextView>(R.id.active_time_stats)
        recipesCreated_textView = rootView.findViewById<TextView>(R.id.recipes_created_stats)
        recipesView_textView = rootView.findViewById<TextView>(R.id.recipes_viewed_stats)
        recipesRated_textView = rootView.findViewById<TextView>(R.id.recipes_rated_stats)
        friends_textView = rootView.findViewById<TextView>(R.id.friends_stats)
        groups_textView = rootView.findViewById<TextView>(R.id.groups_stats)
        barcodesScanned_textView = rootView.findViewById<TextView>(R.id.barcodes_scanned_stats)
        barsVisited_textView = rootView.findViewById<TextView>(R.id.bars_visited_stats)
        gamesPlayed_textView = rootView.findViewById<TextView>(R.id.games_played_stats)

        //Function call to use userName from callback function to set textview
        getUsername { userName ->
            userName_textView.text = userName
        }

        //Function call to use joinDate from callback function to set textview
        getJoinDate { joinDate ->
            joinDate_textView.text = "Member since: $joinDate"
        }

        //Function call to use resultStr from callback function to set textview
        getMembershipLength { resultStr ->
            memberLength_textView.text = "$resultStr"
        }

        //Function call to use membershipLengthHrs from callback function to set textview
        getElapsedActiveTime { membershipLengthHrs ->
            activeTime_textView.text = "$membershipLengthHrs hrs"
        }

        //Function call to use numRecipes from callback function to set textview
        getNumRecipesCreated { numRecipes ->
            recipesCreated_textView.text = numRecipes
        }

        //Function call to use numRecipesView from callback function to set textview
        getNumRecipesViewed { numRecipesView ->
            recipesView_textView.text = numRecipesView
        }

        //Function call to use numRecipesRated from callback function to set textview
        getNumRecipesRated{ numRecipesRated ->
            recipesRated_textView.text = numRecipesRated
        }

        //Function call to use fCount from callback function
        getNumFriends { fCount ->
            friends_textView.text = fCount
        }

        //Function call to use numBarcodesScanned from callback function to set textview
        getNumBarcodesScanned{ numBarcodesScanned ->
            barcodesScanned_textView.text = numBarcodesScanned
        }

        //Function call to use numBarsVisited from callback function
        getNumBarsVisited{ numBarsVisited ->
            barsVisited_textView.text = numBarsVisited
        }

        //Function call to use numGamesPlayed from callback function to set textview
        getNumGamesPlayed{ numGamesPlayed ->
            gamesPlayed_textView.text = numGamesPlayed
        }

        val refreshButton : ImageButton = rootView.findViewById(R.id.refresh_stats_button)
        // Set click listener for the refresh button
        refreshButton.setOnClickListener {
            //Reset textviews to refresh
            refresh() }

        val backButton : ImageButton = rootView.findViewById(R.id.back_button_stats)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        // Set click listener for the back button
        backButton.setOnClickListener(){
            // Navigates the fragment to the more fragment
            navController.navigate(R.id.action_stats_to_more)
        }


        return rootView
    }

    //Reset textviews to refreshReset textviews to refresh
    fun refresh() {
        getUsername { userName ->
            userName_textView.text = userName
        }

        getJoinDate { joinDate ->
            joinDate_textView.text = "Member since: $joinDate"
        }

        getMembershipLength { resultStr ->
            memberLength_textView.text = "$resultStr"
        }

        getElapsedActiveTime { activeTimeHrs ->
            activeTime_textView.text = "$activeTimeHrs hrs"
        }

        getNumRecipesCreated { numRecipes ->
            recipesCreated_textView.text = numRecipes
        }

        getNumRecipesViewed { numRecipesView ->
            recipesView_textView.text = numRecipesView
        }

        getNumRecipesRated{ numRecipesRated ->
            recipesRated_textView.text = numRecipesRated
        }

        getNumFriends { fCount ->
            friends_textView.text = fCount
        }

        getNumBarcodesScanned{ numBarcodesScanned ->
            barcodesScanned_textView.text = numBarcodesScanned
        }

        getNumBarsVisited{ numBarsVisited ->
            barsVisited_textView.text = numBarsVisited
        }

        getNumGamesPlayed{ numGamesPlayed ->
            gamesPlayed_textView.text = numGamesPlayed
        }
    }

    //Callback function to retrieve username
    fun getUsername(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "join_date" field as a timestamp
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val userName = documentSnapshot.getString("userName")
            // Do something with the joinDate value
            callback(userName)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting join date: $exception")
            callback(null)
        }

        /*
        getUsername { userName ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve joindate
    fun getJoinDate(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "join_date" field as a timestamp
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val joinDateTimestamp = documentSnapshot.getTimestamp("join_date")
            //val joinDate = joinDateTimestamp?.toDate()
            val joinDateFormatted = timestampToDate(joinDateTimestamp!!)
            // Do something with the joinDate value
            callback(joinDateFormatted)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting join date: $exception")
            callback(null)
        }

        /*
        getJoinDate { joinDate ->
        // Do something with the membershipLength value
        }
         */
    }

    //Convert timestamp to a formatted date
    fun timestampToDate(ts: Timestamp): String  {
        val firebaseTimestamp: Timestamp = ts
        val date: Date = firebaseTimestamp.toDate()
        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        val formattedDate = formatter.format(date)
        return formattedDate
    }

    //Callback function to retrieve membership length
    fun getMembershipLength(callback: (String?) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "join_date" field as a timestamp
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val joinDateTimestamp = documentSnapshot.getTimestamp("join_date")
            //val joinDate = joinDateTimestamp?.toDate()
            val date1 = joinDateTimestamp!!.toDate()
            val date2 = Timestamp.now().toDate()
            val differenceInMillis: Long = abs(date1.time - date2.time)

            // Calculate the difference in years, months, and days
            val differenceInYears: Int = (differenceInMillis / (1000L * 60L * 60L * 24L * 30L * 12L)).toInt()
            val differenceInMonths: Int = (differenceInMillis / (1000L * 60L * 60L * 24L * 30L) % 12).toInt()
            val differenceInDays: Int = (differenceInMillis / (1000L * 60L * 60L * 24L) % 30).toInt()

            // Construct the result string
            val result = StringBuilder()
            if (differenceInYears > 0) {
                result.append("$differenceInYears year(s), ")
            }
            if (differenceInMonths > 0) {
                result.append("$differenceInMonths month(s), ")
            }
            result.append("$differenceInDays day(s)")

            val resultStr = result.toString()
            // Do something with the joinDate value
            callback(resultStr)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting join date: $exception")
            callback(null)
        }

        /*
        // Retrieve the first timestamp from Firestore, Return an error message if the registerTimestamp field is missing or null
        val userDocument = firestore.collection("UserAccounts").document(userID).get().await()
        val registerTimestamp: Timestamp? = userDocument.getTimestamp("join_date")
            ?: return "Error: registerTimestamp field missing or null"

        // Calculate the difference in milliseconds between the two dates
        val date1: Date = registerTimestamp!!.toDate()
        val date2: Date = timestamp2.toDate()
        val differenceInMillis: Long = abs(date1.time - date2.time)

        // Calculate the difference in years, months, and days
        val differenceInYears: Int = (differenceInMillis / (1000L * 60L * 60L * 24L * 30L * 12L)).toInt()
        val differenceInMonths: Int = (differenceInMillis / (1000L * 60L * 60L * 24L * 30L) % 12).toInt()
        val differenceInDays: Int = (differenceInMillis / (1000L * 60L * 60L * 24L) % 30).toInt()

        // Construct the result string
        val result = StringBuilder()
        if (differenceInYears > 0) {
            result.append("$differenceInYears year(s), ")
        }
        if (differenceInMonths > 0) {
            result.append("$differenceInMonths month(s), ")
        }
        result.append("$differenceInDays day(s)")


        return result.toString()
         */
    }

    //Callback function to retrieve active screen time
    fun getElapsedActiveTime(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "active_time" field
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val activeTimeSec = documentSnapshot.getLong("active_time").toString()
            val activeTimeHrs = String.format("%.2f", activeTimeSec.toFloat() / 3600)
            // Do something with the activeTimeHrs value
            callback(activeTimeHrs)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting membership length: $exception")
            callback(null)
        }

        /*
        getElapsedActiveTime { membershipLengthHrs ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve number of friends
    fun getNumFriends(callback: (String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("UserAccounts").document(userID) // Replace "userId" with the ID of the user document
        val friendsRef = userRef.collection("friends")

        friendsRef.whereEqualTo("status", "Friends").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var fCount = task.result?.size()?.toString() ?: "0"
                // Use the count variable as needed
                callback(fCount)
            } else {
                // Handle any errors
                callback(null)
            }
        }

        /*
        getNumFriends { fCount ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve number of recipes created
    fun getNumRecipesCreated(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "num_recipes_made" field
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val numRecipesMade = documentSnapshot.getLong("num_recipes_made").toString()
            // Do something with the numRecipesMade value
            callback(numRecipesMade)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting numRecipesMade: $exception")
            callback(null)
        }

        /*
        getNumRecipesCreated { numRecipes ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve number of recipes viewed
    fun getNumRecipesViewed(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "num_recipes_viewed" field
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val numRecipesView = documentSnapshot.getLong("num_recipes_viewed").toString()
            // Do something with the membershipLength value
            callback(numRecipesView)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting numRecipesView: $exception")
            callback(null)
        }

        /*
        getNumRecipesViewed { numRecipesView ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve number of recipes rated
    fun getNumRecipesRated(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "num_recipes_rated" field
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val numRecipesRated = documentSnapshot.getLong("num_recipes_rated").toString()
            // Do something with the numRecipesRated value
            callback(numRecipesRated)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting numRecipesRated: $exception")
            callback(null)
        }

        /*
        getNumRecipesRated{ numRecipesRated ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve number of barcodes scanned
    fun getNumBarcodesScanned(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "num_barcodes_scanned" field
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val numBarcodesScanned = documentSnapshot.getLong("num_barcodes_scanned").toString()
            // Do something with the numBarcodesScanned value
            callback(numBarcodesScanned)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting numBarcodesScanned length: $exception")
            callback(null)
        }

        /*
        getNumBarcodesScanned{ numBarcodesScanned ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve number of bars visited
    fun getNumBarsVisited(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "num_bars_visited" field
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val numBarsVisited = documentSnapshot.getLong("num_bars_visited").toString()
            // Do something with the numBarsVisited value
            callback(numBarsVisited)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting numBarsVisited: $exception")
            callback(null)
        }

        /*
        getNumBarsVisited{ numBarsVisited ->
        // Do something with the membershipLength value
        }
         */
    }

    //Callback function to retrieve number of games played
    fun getNumGamesPlayed(callback: (String?) -> Unit) {

        val firestore = FirebaseFirestore.getInstance()
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = firestore.collection("UserAccounts").document(currentUserID ?: "")

        // Retrieve the user's "games_played" field
        userDocumentRef.get().addOnSuccessListener { documentSnapshot ->
            val numGamesPlayed = documentSnapshot.getLong("games_played").toString()
            // Do something with the numGamesPlayed value
            callback(numGamesPlayed)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting numGamesPlayed: $exception")
            callback(null)
        }

        /*
        getNumGamesPlayed{ numGamesPlayed ->
        // Do something with the membershipLength value
        }
         */
    }



}