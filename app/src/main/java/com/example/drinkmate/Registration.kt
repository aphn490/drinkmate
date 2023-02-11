package com.example.drinkmate

import android.content.ContentValues.TAG
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drinkmate.databinding.ActivityMainBinding
import com.example.drinkmate.databinding.ActivityRegistrationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Registration : AppCompatActivity() {

    //initialize variables for all widgets in activity.
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confPass: TextInputEditText
    private lateinit var confButton: Button
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        // set variables to what is currently in the edit text box in activity.
        this.email = findViewById(R.id.email)
        this.password = findViewById(R.id.password)
        this.confPass = findViewById(R.id.confirmPassword)
        this.confButton = findViewById(R.id.Create_Account)
        this.auth = FirebaseAuth.getInstance()

        confButton.setOnClickListener {
            val em = email.text.toString()
            val pas = password.text.toString()
            val cpas = confPass.text.toString()

            if (em.isNotEmpty() && pas.isNotEmpty() && cpas.isNotEmpty()) {
                if (pas == cpas) {
                    auth.createUserWithEmailAndPassword(em, pas)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateUI(null)
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

    private fun regAccount(em: String, pw: String, cpw:String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        if (em.isNotEmpty() && pw.isNotEmpty() && cpw.isNotEmpty()) {
            if (pw == cpw) {
                user["email"] = em
                user["password"] = pw
                db.collection("UserAccounts")
                    .add(user)
                    .addOnSuccessListener {
                        Toast.makeText(this@Registration,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@Registration,"Error Adding Record",Toast.LENGTH_SHORT).show()
                    }
            }
        } else
            Toast.makeText( this@Registration, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
    }

}