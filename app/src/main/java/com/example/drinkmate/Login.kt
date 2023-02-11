package com.example.drinkmate

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.drinkmate.databinding.ActivityMainBinding

class Login : AppCompatActivity() {
    private lateinit var createAcc: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        createAcc = findViewById(R.id.CA)
        createAcc.setOnClickListener{
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
    }

}