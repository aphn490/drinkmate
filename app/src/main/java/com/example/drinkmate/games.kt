package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class games : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_games, container, false)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        // Creates a value that references the kings cup button
        val kingscupButton : Button = view.findViewById(R.id.button1)
        kingscupButton.setOnClickListener(){
            // Navigates the fragment to the kings cup fragment
            navController.navigate(R.id.to_kingscup)
            updateNumOfGamesPlayed()
        }
        // Creates a value that references the taskmaster button
        val taskmasterButton : Button = view.findViewById(R.id.button2)
        taskmasterButton.setOnClickListener(){
            // Navigates the fragment to the taskmaster fragment
            navController.navigate(R.id.to_taskmaster)
            updateNumOfGamesPlayed()
        }
        // Creates a value that references the never have I ever button
        val neverhaveieverButton : Button = view.findViewById(R.id.button3)
        neverhaveieverButton.setOnClickListener(){
            // Navigates the fragment to the bac calculator fragment
            navController.navigate(R.id.to_neverhaveiever)
            updateNumOfGamesPlayed()
        }
        // Creates a value that references the beerpong button
        val beerpongButton : Button = view.findViewById(R.id.button4)
        beerpongButton.setOnClickListener(){
            // Navigates the fragment to the bac calculator fragment
            navController.navigate(R.id.to_beerpong)
            updateNumOfGamesPlayed()
        }
        return view
    }

    fun updateNumOfGamesPlayed() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        val userDocumentRef = FirebaseFirestore.getInstance().collection("UserAccounts").document(currentUserID ?: "")
        userDocumentRef?.update("games_played", FieldValue.increment(1))
    }
}