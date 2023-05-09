package com.example.drinkmate


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator


class Scan : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val scanview = inflater.inflate(R.layout.fragment_scan, container, false)
        val scanButton : Button = scanview.findViewById(R.id.scanButton)
        scanButton.setOnClickListener{
            findNavController().navigate(R.id.action_scan_to_scanner)

            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
            val userDocumentRef = FirebaseFirestore.getInstance().collection("UserAccounts").document(currentUserID ?: "")
            userDocumentRef?.update("num_barcodes_scanned", FieldValue.increment(1))
        }
        return scanview
    }

}