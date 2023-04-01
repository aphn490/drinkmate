package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 * Use the [Drinks.newInstance] factory method to
 * create an instance of this fragment.
 */
class Drinks : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drinks, container, false)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        // Creates a value that references the View Recipes Button
        val recipes : ImageView = view.findViewById(R.id.recipeViewIcon)
        recipes.setOnClickListener(){
            // Navigates the fragment to the recipe view fragment
            navController.navigate(R.id.action_drinks_to_recipe)
        }
        // Creates a value that references the Create Recipes Button
        val createRecipes : ImageView = view.findViewById(R.id.recipeCreationIcon)
        createRecipes.setOnClickListener(){
            // Navigates the fragment to the create recipes fragment
            navController.navigate(R.id.action_drinks_to_recipeCreation)
        }
        return view
    }
}