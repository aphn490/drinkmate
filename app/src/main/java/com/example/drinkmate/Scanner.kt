package com.example.drinkmate

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanIntentResult

class Scanner : AppCompatActivity() {

    private lateinit var scanResult : TextView
    private lateinit var doneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        scanResult = findViewById(R.id.scanResult)
        doneButton = findViewById(R.id.done)

        val scanner = IntentIntegrator(this)

        scanner.initiateScan()

        doneButton.setOnClickListener(){
            this?.onBackPressed()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            scanResult.text = result.contents
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)

        }
    }
}