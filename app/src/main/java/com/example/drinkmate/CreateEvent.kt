package com.example.drinkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var name : TextInputEditText
private lateinit var date : TextInputEditText
private lateinit var time : TextInputEditText
private lateinit var loc : TextInputEditText
private lateinit var desc : TextInputEditText
private lateinit var conf : Button
private lateinit var db : FirebaseFirestore

class CreateEvent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        name = findViewById(R.id.eventName)
        date = findViewById(R.id.eventDate)
        time = findViewById(R.id.eventTime)
        loc = findViewById(R.id.eventLocation)
        desc = findViewById(R.id.eventDescription)
        conf = findViewById(R.id.confirmEvent)

        conf.setOnClickListener {
            val n = name.text.toString()
            val d = date.text.toString()
            val t = time.text.toString()
            val l = loc.text.toString()
            val de = desc.text.toString()
            val bundle : Bundle?= intent.extras
            val gn = bundle?.getString("GroupName")
            db = FirebaseFirestore.getInstance()

            if (n.isNotEmpty() && d.isNotEmpty() && t.isNotEmpty() && l.isNotEmpty() && de.isNotEmpty()) {

                val event = hashMapOf(
                    "EventName" to n,
                    "Date" to d,
                    "time" to t,
                    "location" to l,
                    "Description" to de
                )

                db.collection("Groups").document(gn!!).collection("events").document(n).set(event)

                Toast.makeText( this@CreateEvent, "Event Created", Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText( this@CreateEvent, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}