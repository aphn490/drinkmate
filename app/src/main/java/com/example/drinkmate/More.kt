package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation

class More : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        // Creates a value that references the Blood Alcohol Content Calculator Button
        val bacButton : TextView = view.findViewById(R.id.bacDescription)
        bacButton.setOnClickListener(){
            // Navigates the fragment to the bac calculator fragment
            navController.navigate(R.id.action_more_to_calculator)
        }
        // Creates a value that references the Resources to fight Addiction Button
        val resButton : TextView = view.findViewById(R.id.addictionDescription)
        resButton.setOnClickListener(){
            // Navigates the fragment to the addiction resources fragment
            navController.navigate(R.id.action_more_to_addiction)
        }

        // Creates a value that references the Congestion Button
        val congestionButton : TextView = view.findViewById(R.id.congestionDescription)
        congestionButton.setOnClickListener(){
            // Navigates the fragment to the addiction resources fragment
            navController.navigate(R.id.action_more_to_congestion)
        }

        // Creates a value that references the drinking games Button
        val gameButton : TextView = view.findViewById(R.id.gameDescription)
        gameButton.setOnClickListener(){
            // Navigates the fragment to the drinking games fragment
            navController.navigate(R.id.action_more_to_games)
        }
        // Creates a value that references the chat button
        val chatButton : TextView = view.findViewById(R.id.chatDescription)
        chatButton.setOnClickListener() {
            // Navigates the fragment to the chat fragment
            navController.navigate(R.id.action_more_to_chat)
        }

        val shopButton : TextView = view.findViewById(R.id.shoppingDescription)
        shopButton.setOnClickListener() {
            // Navigates the fragment to the chat fragment
            navController.navigate(R.id.action_more_to_shopping)
        }
        return view
    }
}