package com.example.drinkmate

import android.annotation.SuppressLint
//import android.app.NotificationChannel
//import android.app.NotificationManager
import android.content.ContentValues
import android.content.ContentValues.TAG
//import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
//import android.os.Build
import android.os.Bundle
import android.util.Log
//import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
//import com.example.drinkmate.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging

class Login : AppCompatActivity() {
    private lateinit var createAcc: Button
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: Button
    private lateinit var auth: FirebaseAuth
    private var userDevice: String = ""


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        createAcc = findViewById(R.id.CA)
        createAcc.setOnClickListener{
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        login.setOnClickListener{
            val em = email.text.toString()
            val pas = password.text.toString()
            if (em.isNotEmpty() && pas.isNotEmpty()){
                auth.signInWithEmailAndPassword(em, pas)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Toast.makeText( this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)

                            val firebaseUser = auth.currentUser
                            val editor = getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit()
                            val firebaseUserId = firebaseUser?.uid
                            editor.putString("uid", firebaseUserId)
                            editor.apply()

                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                // Get new FCM registration token
                                val token = task.result
                                userDevice = token
                                println(userDevice)

                                val db = FirebaseFirestore.getInstance()

                                val data = hashMapOf("deviceToken" to token)
                                    auth.currentUser?.let { it1 ->
                                        db.collection("UserAccounts").document(
                                            it1.uid).set(data, SetOptions.merge())
                                    }

                                    /*
                                    auth.currentUser?.let { it1 ->
                                        db.collection("UserAccounts").document(
                                            it1.uid).collection("friends").get()
                                            .addOnSuccessListener { querySnapshot ->
                                                // iterate through each document in the collection
                                                for (document in querySnapshot.documents) {
                                                    document.reference.update("can_view_location", false)
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.d(TAG, "Error getting documents: $exception")
                                            }
                                    }

                                     */

                                val userAccountRef = db.collection("UserAccounts").document(FirebaseAuth.getInstance().currentUser?.uid ?: "").collection("friends")
                                    userAccountRef.get()
                                        .addOnSuccessListener { querySnapshot ->
                                            for (document in querySnapshot) {
                                                val data = document.data
                                                // Perform operations on the document data
                                                document.reference.set(mapOf("can_view_location" to true), SetOptions.merge())
                                            }

                                        }

                                //println("LOGIN START ============================================================")
                                //startLocationService()

                                // Log and toast
                                Log.d(ContentValues.TAG, token)
                                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                            })




                            startActivity(intent)

                        } else {
                            Toast.makeText( this@Login, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText( this@Login, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}