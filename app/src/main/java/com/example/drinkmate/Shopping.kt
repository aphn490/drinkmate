package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 * Use the [Shopping.newInstance] factory method to
 * create an instance of this fragment.
 */
class Shopping : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val navButton = view.findViewById<Button>(R.id.createShopping)
        navButton.setOnClickListener(){
            navController.navigate(R.id.action_shopping_to_shoppingCreation)
        }
    }
}