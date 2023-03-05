package com.example.drinkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class PersonAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_account)

        val viewAccName : TextView = findViewById(R.id.accName)

        val bundle : Bundle?= intent.extras

        val email = bundle!!.getString("email")

        viewAccName.text = email
    }
}