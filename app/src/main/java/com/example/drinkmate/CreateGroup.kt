package com.example.drinkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var gn : TextInputEditText
private lateinit var gd : TextInputEditText
private lateinit var button: Button
private lateinit var db : FirebaseFirestore

class CreateGroup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        gn = findViewById(R.id.GroupName)
        gd = findViewById(R.id.GroupDesc)
        button = findViewById(R.id.confirm)
        db = FirebaseFirestore.getInstance()

        button.setOnClickListener {
            val n = gn.text.toString()
            val d = gd.text.toString()

            if (n.isNotEmpty() && d.isNotEmpty()){
                val group = hashMapOf(
                    "GroupName" to n,
                    "Description" to d,
                    "NumberOfMembers" to 0,
                    "image" to ""
                )
                db.collection("Groups").document(n).set(group)

                Toast.makeText( this@CreateGroup, "Group Created", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText( this@CreateGroup, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}