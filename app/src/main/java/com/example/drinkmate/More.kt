package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

class More : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val button : Button = view.findViewById(R.id.bacButton)
        button.setOnClickListener(){
            navController.navigate(R.id.action_more_to_calculator)
        }
        return view
    }
}