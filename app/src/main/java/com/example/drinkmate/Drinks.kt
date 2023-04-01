package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

class Drinks : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drinks, container, false)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val searchButton : Button = view.findViewById(R.id.searchButton)
        searchButton.setOnClickListener(){
            // Navigates the fragment to the drink search fragment
            navController.navigate(R.id.action_drinks_to_drinkSearch)
        }
        return view
    }
}