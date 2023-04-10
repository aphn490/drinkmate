package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

// Recipe fragment that lists the recipes you've created and other recipes that you can search with
class Recipe : Fragment() {
    // Initializes each recipe as a hashmap
    private val db = Firebase.firestore
    private var recipe1: HashMap<String, *>? = null
    private var recipe2: HashMap<String, *>? = null
    private var recipe3: HashMap<String, *>? = null
    private var recipe4: HashMap<String, *>? = null
    private var recipe5: HashMap<String, *>? = null
    private var recipe6: HashMap<String, *>? = null
    private var recipe7: HashMap<String, *>? = null
    private var recipe8: HashMap<String, *>? = null
    private var recipe9: HashMap<String, *>? = null
    private var recipe10: HashMap<String, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Function to change image of an ImageView based on string
    fun changeImage(iv : ImageView, st : String) {
        if (st == "Whiskey") {
            iv.setImageResource(R.drawable.whiskey)
        } else if (st == "Gin") {
            iv.setImageResource(R.drawable.gin)
        } else if (st == "Tequila") {
            iv.setImageResource(R.drawable.tequila)
        } else if (st == "Rum") {
            iv.setImageResource(R.drawable.rum)
        } else if (st == "Brandy") {
            iv.setImageResource(R.drawable.brandy)
        } else if (st == "Cognac") {
            iv.setImageResource(R.drawable.cognac)
        } else if (st == "Scotch") {
            iv.setImageResource(R.drawable.scotch)
        } else if (st == "Vodka") {
            iv.setImageResource(R.drawable.vodka)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Creates the navController that will let the user navigate to CongestionMap fragment.
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val recipe1image = view.findViewById<ImageView>(R.id.recipe1)
        val recipe2image = view.findViewById<ImageView>(R.id.recipe2)
        val recipe3image = view.findViewById<ImageView>(R.id.recipe3)
        val recipe4image = view.findViewById<ImageView>(R.id.recipe4)
        val recipe5image = view.findViewById<ImageView>(R.id.recipe5)
        val nextButton = view.findViewById<Button>(R.id.nextRecipes)
        val prevButton = view.findViewById<Button>(R.id.prevRecipes)

        // Fetches information from the database
        val yourRecipes =
            db.collection("Recipes").document(FirebaseAuth.getInstance().uid.toString()).get()
                .addOnSuccessListener {
                    // Once the fetching is done, this will assign the data that's retrieved into the variables that were created earlier
                    if (it.get("recipe1") != null) {
                        recipe1 = it.get("recipe1") as HashMap<String, *>
                    }
                    if (it.get("recipe2") != null) {
                        recipe2 = it.get("recipe2") as HashMap<String, *>
                    }
                    if (it.get("recipe3") != null) {
                        recipe3 = it.get("recipe3") as HashMap<String, *>
                    }
                    if (it.get("recipe4") != null) {
                        recipe4 = it.get("recipe4") as HashMap<String, *>
                    }
                    if (it.get("recipe5") != null) {
                        recipe5 = it.get("recipe5") as HashMap<String, *>
                    }
                    if (it.get("recipe6") != null) {
                        recipe6 = it.get("recipe6") as HashMap<String, *>
                    }
                    if (it.get("recipe7") != null) {
                        recipe7 = it.get("recipe7") as HashMap<String, *>
                    }
                    if (it.get("recipe8") != null) {
                        recipe8 = it.get("recipe8") as HashMap<String, *>
                    }
                    if (it.get("recipe9") != null) {
                        recipe9 = it.get("recipe9") as HashMap<String, *>
                    }
                    if (it.get("recipe10") != null) {
                        recipe10 = it.get("recipe10") as HashMap<String, *>
                    }
                }.addOnCompleteListener {
                    // Once the assigning of data is done, it will proceed to go on to this code.
                    if (it.isSuccessful) {
                        // Checks to see if each recipe exists and, if it does, ...
                        // Set each recipe name description to the name and each image to the
                        // preset image based on the main ingredient.
                        if (recipe1 != null) {
                            view.findViewById<TextView>(R.id.recipe1name).text = recipe1!!["name"] as String
                            var img = recipe1!!["mainIngredient"] as String
                            changeImage(recipe1image, img)
                            view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
                        } else {
                            view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                        }
                        if (recipe2 != null) {
                            view.findViewById<TextView>(R.id.recipe2name).text = recipe2!!["name"] as String
                            var img = recipe2!!["mainIngredient"] as String
                            changeImage(recipe2image, img)
                            view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
                        } else {
                            view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                        }
                        if (recipe3 != null){
                            view.findViewById<TextView>(R.id.recipe3name).text = recipe3!!["name"] as String
                            var img = recipe3!!["mainIngredient"] as String
                            changeImage(recipe3image, img)
                            view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
                        } else {
                            view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                        }
                        if (recipe4 != null){
                            view.findViewById<TextView>(R.id.recipe4name).text = recipe4!!["name"] as String
                            var img = recipe4!!["mainIngredient"] as String
                            changeImage(recipe4image, img)
                            view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
                        } else {
                            view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                        }
                        if (recipe5 != null){
                            view.findViewById<TextView>(R.id.recipe5name).text = recipe5!!["name"] as String
                            var img = recipe5!!["mainIngredient"] as String
                            changeImage(recipe5image, img)
                            view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
                        } else {
                            // Hides the layout that has the bar image + name just in case doesn't have 5 favorite bars
                            view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                        }
                    }
                }
        // Sets visibility of prevbutton to gone
        prevButton.visibility = View.GONE

        // Sets an on-click listener to the next page button
        nextButton.setOnClickListener() {
            // Changes each image and name to recipes 6-10 if they exist.
            if (recipe6 != null) {
                view.findViewById<TextView>(R.id.recipe1name).text = recipe6!!["name"] as String
                var img = recipe6!!["mainIngredient"] as String
                changeImage(recipe1image, img)
                view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
            }
            if (recipe7 != null) {
                view.findViewById<TextView>(R.id.recipe2name).text = recipe7!!["name"] as String
                var img = recipe7!!["mainIngredient"] as String
                changeImage(recipe2image, img)
                view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
            }
            if (recipe8 != null){
                view.findViewById<TextView>(R.id.recipe3name).text = recipe8!!["name"] as String
                var img = recipe8!!["mainIngredient"] as String
                changeImage(recipe3image, img)
                view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
            }
            if (recipe9 != null){
                view.findViewById<TextView>(R.id.recipe4name).text = recipe9!!["name"] as String
                var img = recipe9!!["mainIngredient"] as String
                changeImage(recipe4image, img)
                view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
            }
            if (recipe10 != null){
                view.findViewById<TextView>(R.id.recipe5name).text = recipe10!!["name"] as String
                var img = recipe10!!["mainIngredient"] as String
                changeImage(recipe5image, img)
                view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
            } else {
                // Hides the layout that has the bar image + name just in case doesn't have 5 recipes
                view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
            }
            // Makes next page button invisible while making prev button visible
            nextButton.visibility = View.GONE
            prevButton.visibility = View.VISIBLE
        }
        // Sets an on-click listener to the previous  page button
        prevButton.setOnClickListener() {
            // Changes each image and name to recipes 1-5 if they exist.
            if (recipe1 != null) {
                view.findViewById<TextView>(R.id.recipe1name).text = recipe1!!["name"] as String
                var img = recipe1!!["mainIngredient"] as String
                changeImage(recipe1image, img)
                view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
            }
            if (recipe2 != null) {
                view.findViewById<TextView>(R.id.recipe2name).text = recipe2!!["name"] as String
                var img = recipe2!!["mainIngredient"] as String
                changeImage(recipe2image, img)
                view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
            }
            if (recipe3 != null){
                view.findViewById<TextView>(R.id.recipe3name).text = recipe3!!["name"] as String
                var img = recipe3!!["mainIngredient"] as String
                changeImage(recipe3image, img)
                view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
            }
            if (recipe4 != null){
                view.findViewById<TextView>(R.id.recipe4name).text = recipe4!!["name"] as String
                var img = recipe4!!["mainIngredient"] as String
                changeImage(recipe4image, img)
                view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
            }
            if (recipe5 != null){
                view.findViewById<TextView>(R.id.recipe5name).text = recipe5!!["name"] as String
                var img = recipe5!!["mainIngredient"] as String
                changeImage(recipe5image, img)
                view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
            } else {
                // Hides the layout that has the bar image + name just in case doesn't have recipe
                view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
            }
            nextButton.visibility = View.VISIBLE
            prevButton.visibility = View.GONE
        }

        // For each recipe image, if the user clicks on it, then it navigates to the View Recipe page
        // and sends the necessary hashmap to the fragment result
        recipe1image.setOnClickListener() {
            if (nextButton.visibility == View.VISIBLE) {
                setFragmentResult("recipeKey", bundleOf("list" to recipe1))
            } else {
                setFragmentResult("recipeKey", bundleOf("list" to recipe6))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe2image.setOnClickListener() {
            if (nextButton.visibility == View.VISIBLE) {
                setFragmentResult("recipeKey", bundleOf("list" to recipe2))
            } else {
                setFragmentResult("recipeKey", bundleOf("list" to recipe7))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe3image.setOnClickListener() {
            if (nextButton.visibility == View.VISIBLE) {
                setFragmentResult("recipeKey", bundleOf("list" to recipe3))
            } else {
                setFragmentResult("recipeKey", bundleOf("list" to recipe8))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe4image.setOnClickListener() {
            if (nextButton.visibility == View.VISIBLE) {
                setFragmentResult("recipeKey", bundleOf("list" to recipe4))
            } else {
                setFragmentResult("recipeKey", bundleOf("list" to recipe9))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe5image.setOnClickListener() {
            if (nextButton.visibility == View.VISIBLE) {
                setFragmentResult("recipeKey", bundleOf("list" to recipe5))
            } else {
                setFragmentResult("recipeKey", bundleOf("list" to recipe10))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
    }
}