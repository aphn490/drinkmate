package com.example.drinkmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Registration : AppCompatActivity() {

    //initialize variables for all widgets in activity.
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confPass: TextInputEditText
    private lateinit var confButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        // set variables to what is currently in the edit text box in activity.
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        confPass = findViewById(R.id.confirmPassword)
        confButton = findViewById(R.id.Create_Account)
        auth = FirebaseAuth.getInstance()
        textView = findViewById(R.id.loginNow)
        val db = Firebase.firestore

        textView.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        confButton.setOnClickListener {
            val em = email.text.toString()
            val pas = password.text.toString()
            val cpas = confPass.text.toString()

            if (em.isNotEmpty() && pas.isNotEmpty() && cpas.isNotEmpty()) {
                if (pas == cpas) {
                    auth.createUserWithEmailAndPassword(em, pas)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText( this@Registration, "Account Created", Toast.LENGTH_SHORT).show()
                                val user = hashMapOf(
                                    "UID" to (auth.uid),
                                    "email" to em
                                )
                                auth.uid?.let { it1 -> db.collection("UserAccounts").document(it1).set(user) }

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText( this@Registration, "Authentication Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText( this@Registration, "Passwords Do Not Match", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText( this@Registration, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}