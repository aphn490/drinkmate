package com.example.drinkmate

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeCreation.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeCreation : Fragment() {
    private val db = Firebase.firestore
    private var currentuid = FirebaseAuth.getInstance().currentUser?.uid
    private var allIngredients : MutableList<String> = ArrayList()
    private var allSteps : MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val button = view.findViewById<Button>(R.id.recipeCreate)
        val name = view.findViewById<EditText>(R.id.recipeName)
        val ingredients = view.findViewById<EditText>(R.id.ingredientsList)
        val steps = view.findViewById<EditText>(R.id.stepsList)
        val spinner = view.findViewById<Spinner>(R.id.mainIngredient)
        val mainIngredientOptions = arrayOf("Whiskey", "Gin", "Tequila", "Rum", "Brandy", "Cognac", "Scotch", "Vodka")
        val addIngredButton = view.findViewById<Button>(R.id.addIngredient)
        val addStepButton = view.findViewById<Button>(R.id.addStep)
        val removeIngred = view.findViewById<Button>(R.id.removeIngredients)
        val removeStep = view.findViewById<Button>(R.id.removeSteps)

        var ingredientsString = ""
        var stepsString = ""

        if (spinner != null) {
            // Sets the spinner's values to the previously created values.
            val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, mainIngredientOptions)
            spinner.adapter = arrayAdapter
            }

        addIngredButton.setOnClickListener() {
            if (ingredients.text.toString() != "") {
                allIngredients.add(ingredients.text.toString())
                Toast.makeText(requireContext(), "Added " + ingredients.text.toString() + " to Ingredients.", Toast.LENGTH_SHORT).show()
                ingredients.setText("")
            }
        }

        addStepButton.setOnClickListener() {
            if (steps.text.toString() != "") {
                allSteps.add(steps.text.toString())
                Toast.makeText(requireContext(), "Added " + steps.text.toString() + " to Steps.", Toast.LENGTH_SHORT).show()
                steps.setText("")
            }
        }

        removeIngred.setOnClickListener() {
            if(allIngredients.isNotEmpty()) {
                Toast.makeText(requireContext(), "Removed " + allIngredients.get(allIngredients.size - 1) + " from Ingredients.", Toast.LENGTH_SHORT).show()
                allIngredients.removeAt(allIngredients.size - 1)
            } else {
                Toast.makeText(requireContext(), "No Ingredients Added Yet.", Toast.LENGTH_SHORT).show()
            }
        }

        removeStep.setOnClickListener() {
            if(allSteps.isNotEmpty()) {
                Toast.makeText(requireContext(), "Removed " + allSteps.get(allSteps.size - 1) + " from steps.", Toast.LENGTH_SHORT).show()
                allSteps.removeAt(allSteps.size - 1)
            } else {
                Toast.makeText(requireContext(), "No Steps Added Yet.", Toast.LENGTH_SHORT).show()
            }
        }
        button?.setOnClickListener() {
            ingredientsString = ""
            stepsString = ""
            for (x in allIngredients) {
                ingredientsString += "$x |"
            }
            for (x in allSteps) {
                stepsString += "$x |"
            }
            if (name.text.toString()!="" && allIngredients.isNotEmpty() && allSteps.isNotEmpty()) {
                val rec = hashMapOf (
                    "name" to name.text.toString(),
                    "mainIngredient" to spinner.selectedItem.toString(),
                    "ingredients" to ingredientsString,
                    "steps" to stepsString
                )
                System.out.println("TESTING HERE")
                System.out.println(name.text.toString())
                System.out.println(spinner.selectedItem.toString())
                System.out.println(ingredients.text.toString())
                System.out.println(steps.text.toString())
                // Brings up the document under the Recipes collection that has the user's id from firebase
                val recipeRef = db.collection("Recipes").document(currentuid.toString())
                recipeRef.get().addOnCompleteListener {
                        task ->
                    //Once the task is completed, it'll undergo the following code
                    if (task.isSuccessful) {
                        val doc = task.result
                        // If recipe 1 doesn't exist yet, it will update the document with the bar details under
                        // the name "recipe1"
                        if (doc.get("recipe1") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe1" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 2 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe2"
                        } else if (doc.get("recipe2") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe2" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 3 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe3"
                        } else if (doc.get("recipe3") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe3" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 4 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe4"
                        } else if (doc.get("recipe4") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe4" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 5 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe5"
                        } else if (doc.get("recipe5") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe5" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 6 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe6"
                        } else if (doc.get("recipe6") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe6" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 7 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe7"
                        } else if (doc.get("recipe7") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe7" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 8 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe8"
                        } else if (doc.get("recipe8") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe8" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 9 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe9"
                        } else if (doc.get("recipe9") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe9" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                            // If recipe 10 doesn't exist yet, it will update the document with the bar details under
                            // the name "recipe10"
                        } else if (doc.get("recipe10") == null) {
                            val recipeCreated = hashMapOf (
                                "recipe10" to rec
                            )
                            db.collection("Recipes").document(currentuid.toString()).set(recipeCreated, SetOptions.merge())
                        } else {
                            Toast.makeText(requireContext(), "Max Recipes reached.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d(ContentValues.TAG, "get failed with ", task.exception)
                    }
                }
                activity?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Please Fill In All Of The Fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}