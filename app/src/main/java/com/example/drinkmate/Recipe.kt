package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private val friendsIds = mutableListOf<String>("Index 0")
    private val friendsEmails = mutableListOf<String>("Please Select Your Friend Here")
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
    private var friendsRecipe1: HashMap<String, *>? = null
    private var friendsRecipe2: HashMap<String, *>? = null
    private var friendsRecipe3: HashMap<String, *>? = null
    private var friendsRecipe4: HashMap<String, *>? = null
    private var friendsRecipe5: HashMap<String, *>? = null
    private var friendsRecipe6: HashMap<String, *>? = null
    private var friendsRecipe7: HashMap<String, *>? = null
    private var friendsRecipe8: HashMap<String, *>? = null
    private var friendsRecipe9: HashMap<String, *>? = null
    private var friendsRecipe10: HashMap<String, *>? = null
    private val uid = FirebaseAuth.getInstance().uid.toString()
    private var friendsActive = false

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
        val yourRecipes = view.findViewById<Button>(R.id.yourRecipes)
        val friendsButton = view.findViewById<Button>(R.id.friendsRecipes)
        val friendsSpinner = view.findViewById<Spinner>(R.id.friendsSpinner)
        val recipeSpace = view.findViewById<Space>(R.id.recipeSpace)
        var img = ""
        friendsSpinner.visibility = View.GONE

        // Fetches information from the database
        db.collection("Recipes").document(uid).get()
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
                        img = recipe1!!["mainIngredient"] as String
                        changeImage(recipe1image, img)
                        view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
                    } else {
                        view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                    }
                    if (recipe2 != null) {
                        view.findViewById<TextView>(R.id.recipe2name).text = recipe2!!["name"] as String
                        img = recipe2!!["mainIngredient"] as String
                        changeImage(recipe2image, img)
                        view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
                    } else {
                        view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                    }
                    if (recipe3 != null){
                        view.findViewById<TextView>(R.id.recipe3name).text = recipe3!!["name"] as String
                        img = recipe3!!["mainIngredient"] as String
                        changeImage(recipe3image, img)
                        view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
                    } else {
                        view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                    }
                    if (recipe4 != null){
                        view.findViewById<TextView>(R.id.recipe4name).text = recipe4!!["name"] as String
                        img = recipe4!!["mainIngredient"] as String
                        changeImage(recipe4image, img)
                        view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
                    } else {
                        view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                    }
                    if (recipe5 != null){
                        view.findViewById<TextView>(R.id.recipe5name).text = recipe5!!["name"] as String
                        img = recipe5!!["mainIngredient"] as String
                        changeImage(recipe5image, img)
                        view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
                    } else {
                        // Hides the layout that has the bar image + name just in case doesn't have 5 favorite bars
                        view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                    }
                }
            }

        db.collection("UserAccounts").document(uid).collection("friends").get()
            .addOnSuccessListener {
                friendsIds.clear()
                friendsEmails.clear()
                friendsIds.add("Index 0")
                friendsEmails.add("Please Select Your Friend Here")
                it.forEach { document ->
                    val id = document.id
                    val email = document.get("email").toString()
                    friendsIds.add(id)
                    friendsEmails.add(email)
                }
            }.addOnCompleteListener {
                friendsButton.setOnClickListener {
                    friendsActive = true
                    recipeSpace.visibility = View.GONE
                    friendsSpinner.visibility = View.VISIBLE
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                    val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, friendsEmails)
                    println("PRINTING FRIENDS EMAILS")
                    println(friendsEmails)
                    friendsSpinner.adapter = arrayAdapter
                }
                friendsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        resetFriendsRecipes()
                        if (p2 > 0) {
                            Toast.makeText(requireContext(), "Loading " + friendsEmails[p2] + "'s Recipes", Toast.LENGTH_SHORT).show()
                            db.collection("Recipes").document(friendsIds[p2]).get()
                            .addOnSuccessListener {
                                if (it.get("recipe1") != null) {
                                    friendsRecipe1 = it.get("recipe1") as HashMap<String, *>
                                }
                                if (it.get("recipe2") != null) {
                                    friendsRecipe2 = it.get("recipe2") as HashMap<String, *>
                                }
                                if (it.get("recipe3") != null) {
                                    friendsRecipe3 = it.get("recipe3") as HashMap<String, *>
                                }
                                if (it.get("recipe4") != null) {
                                    friendsRecipe4 = it.get("recipe4") as HashMap<String, *>
                                }
                                if (it.get("recipe5") != null) {
                                    friendsRecipe5 = it.get("recipe5") as HashMap<String, *>
                                }
                                if (it.get("recipe6") != null) {
                                    friendsRecipe6 = it.get("recipe6") as HashMap<String, *>
                                }
                                if (it.get("recipe7") != null) {
                                    friendsRecipe7 = it.get("recipe7") as HashMap<String, *>
                                }
                                if (it.get("recipe8") != null) {
                                    friendsRecipe8 = it.get("recipe8") as HashMap<String, *>
                                }
                                if (it.get("recipe9") != null) {
                                    friendsRecipe9 = it.get("recipe9") as HashMap<String, *>
                                }
                                if (it.get("recipe10") != null) {
                                    friendsRecipe10 = it.get("recipe10") as HashMap<String, *>
                                }
                            }.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    if (friendsRecipe1 != null) {
                                        view.findViewById<TextView>(R.id.recipe1name).text = friendsRecipe1!!["name"] as String
                                        val img = friendsRecipe1!!["mainIngredient"] as String
                                        changeImage(recipe1image, img)
                                        view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
                                    } else {
                                        view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                                    }
                                    if (friendsRecipe2 != null) {
                                        view.findViewById<TextView>(R.id.recipe2name).text = friendsRecipe2!!["name"] as String
                                        val img = friendsRecipe2!!["mainIngredient"] as String
                                        changeImage(recipe2image, img)
                                        view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
                                    } else {
                                        view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                                    }
                                    if (friendsRecipe3 != null) {
                                        view.findViewById<TextView>(R.id.recipe3name).text = friendsRecipe3!!["name"] as String
                                        val img = friendsRecipe3!!["mainIngredient"] as String
                                        changeImage(recipe3image, img)
                                        view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
                                    } else {
                                        view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                                    }
                                    if (friendsRecipe4 != null) {
                                        view.findViewById<TextView>(R.id.recipe4name).text = friendsRecipe4!!["name"] as String
                                        val img = friendsRecipe4!!["mainIngredient"] as String
                                        changeImage(recipe4image, img)
                                        view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
                                    } else {
                                        view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                                    }
                                    if (friendsRecipe5 != null) {
                                        view.findViewById<TextView>(R.id.recipe5name).text = friendsRecipe5!!["name"] as String
                                        val img = friendsRecipe5!!["mainIngredient"] as String
                                        changeImage(recipe5image, img)
                                        view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
                                    } else {
                                        view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                                    }
                                }
                            }
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }

        yourRecipes.setOnClickListener() {
            friendsActive = false
            recipeSpace.visibility = View.VISIBLE
            friendsSpinner.visibility = View.GONE
            if (recipe1 != null) {
                view.findViewById<TextView>(R.id.recipe1name).text = recipe1!!["name"] as String
                img = recipe1!!["mainIngredient"] as String
                changeImage(recipe1image, img)
                view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
            }
            if (recipe2 != null) {
                view.findViewById<TextView>(R.id.recipe2name).text = recipe2!!["name"] as String
                img = recipe2!!["mainIngredient"] as String
                changeImage(recipe2image, img)
                view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
            }
            if (recipe3 != null){
                view.findViewById<TextView>(R.id.recipe3name).text = recipe3!!["name"] as String
                img = recipe3!!["mainIngredient"] as String
                changeImage(recipe3image, img)
                view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
            }
            if (recipe4 != null){
                view.findViewById<TextView>(R.id.recipe4name).text = recipe4!!["name"] as String
                img = recipe4!!["mainIngredient"] as String
                changeImage(recipe4image, img)
                view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
            } else {
                view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
            }
            if (recipe5 != null){
                view.findViewById<TextView>(R.id.recipe5name).text = recipe5!!["name"] as String
                img = recipe5!!["mainIngredient"] as String
                changeImage(recipe5image, img)
                view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
            } else {
                // Hides the layout that has the bar image + name just in case doesn't have 5 favorite bars
                view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
            }
        }
        // Sets visibility of prevbutton to gone
        prevButton.visibility = View.GONE

        // Sets an on-click listener to the next page button
        nextButton.setOnClickListener() {
            // Changes each image and name to recipes 6-10 if they exist.
            if(friendsActive == false) {
                if (recipe6 != null) {
                    view.findViewById<TextView>(R.id.recipe1name).text = recipe6!!["name"] as String
                    img = recipe6!!["mainIngredient"] as String
                    changeImage(recipe1image, img)
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                }
                if (recipe7 != null) {
                    view.findViewById<TextView>(R.id.recipe2name).text = recipe7!!["name"] as String
                    img = recipe7!!["mainIngredient"] as String
                    changeImage(recipe2image, img)
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                }
                if (recipe8 != null){
                    view.findViewById<TextView>(R.id.recipe3name).text = recipe8!!["name"] as String
                    img = recipe8!!["mainIngredient"] as String
                    changeImage(recipe3image, img)
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                }
                if (recipe9 != null){
                    view.findViewById<TextView>(R.id.recipe4name).text = recipe9!!["name"] as String
                    img = recipe9!!["mainIngredient"] as String
                    changeImage(recipe4image, img)
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                }
                if (recipe10 != null){
                    view.findViewById<TextView>(R.id.recipe5name).text = recipe10!!["name"] as String
                    img = recipe10!!["mainIngredient"] as String
                    changeImage(recipe5image, img)
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
                } else {
                    // Hides the layout that has the bar image + name just in case doesn't have 5 recipes
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                }
            } else {
                if (friendsRecipe6 != null) {
                    view.findViewById<TextView>(R.id.recipe1name).text = friendsRecipe6!!["name"] as String
                    img = friendsRecipe6!!["mainIngredient"] as String
                    changeImage(recipe1image, img)
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                }
                if (friendsRecipe7 != null) {
                    view.findViewById<TextView>(R.id.recipe2name).text = friendsRecipe7!!["name"] as String
                    img = friendsRecipe7!!["mainIngredient"] as String
                    changeImage(recipe2image, img)
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                }
                if (friendsRecipe8 != null){
                    view.findViewById<TextView>(R.id.recipe3name).text = friendsRecipe8!!["name"] as String
                    img = friendsRecipe8!!["mainIngredient"] as String
                    changeImage(recipe3image, img)
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                }
                if (friendsRecipe9 != null){
                    view.findViewById<TextView>(R.id.recipe4name).text = friendsRecipe9!!["name"] as String
                    img = friendsRecipe9!!["mainIngredient"] as String
                    changeImage(recipe4image, img)
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                }
                if (friendsRecipe10 != null){
                    view.findViewById<TextView>(R.id.recipe5name).text = friendsRecipe10!!["name"] as String
                    img = friendsRecipe10!!["mainIngredient"] as String
                    changeImage(recipe5image, img)
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
                } else {
                    // Hides the layout that has the bar image + name just in case doesn't have 5 recipes
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                }
            }

            // Makes next page button invisible while making prev button visible
            nextButton.visibility = View.GONE
            prevButton.visibility = View.VISIBLE
        }
        // Sets an on-click listener to the previous  page button
        prevButton.setOnClickListener() {
            // Changes each image and name to recipes 1-5 if they exist.
            if (friendsActive == false) {
                if (recipe1 != null) {
                    view.findViewById<TextView>(R.id.recipe1name).text = recipe1!!["name"] as String
                    img = recipe1!!["mainIngredient"] as String
                    changeImage(recipe1image, img)
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                }
                if (recipe2 != null) {
                    view.findViewById<TextView>(R.id.recipe2name).text = recipe2!!["name"] as String
                    img = recipe2!!["mainIngredient"] as String
                    changeImage(recipe2image, img)
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                }
                if (recipe3 != null){
                    view.findViewById<TextView>(R.id.recipe3name).text = recipe3!!["name"] as String
                    img = recipe3!!["mainIngredient"] as String
                    changeImage(recipe3image, img)
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                }
                if (recipe4 != null){
                    view.findViewById<TextView>(R.id.recipe4name).text = recipe4!!["name"] as String
                    img = recipe4!!["mainIngredient"] as String
                    changeImage(recipe4image, img)
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                }
                if (recipe5 != null){
                    view.findViewById<TextView>(R.id.recipe5name).text = recipe5!!["name"] as String
                    img = recipe5!!["mainIngredient"] as String
                    changeImage(recipe5image, img)
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
                } else {
                    // Hides the layout that has the bar image + name just in case doesn't have recipe
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                }
            } else {
                if (friendsRecipe1 != null) {
                    view.findViewById<TextView>(R.id.recipe1name).text = friendsRecipe1!!["name"] as String
                    img = friendsRecipe1!!["mainIngredient"] as String
                    changeImage(recipe1image, img)
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe1layout).visibility = View.GONE
                }
                if (friendsRecipe2 != null) {
                    view.findViewById<TextView>(R.id.recipe2name).text = friendsRecipe2!!["name"] as String
                    img = friendsRecipe2!!["mainIngredient"] as String
                    changeImage(recipe2image, img)
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.VISIBLE
                } else {
                    println("FRIENDS 2 NULL")
                    view.findViewById<LinearLayout>(R.id.recipe2layout).visibility = View.GONE
                }
                if (friendsRecipe3 != null) {
                    view.findViewById<TextView>(R.id.recipe3name).text = friendsRecipe3!!["name"] as String
                    img = friendsRecipe3!!["mainIngredient"] as String
                    changeImage(recipe3image, img)
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe3layout).visibility = View.GONE
                }
                if (friendsRecipe4 != null) {
                    view.findViewById<TextView>(R.id.recipe4name).text = friendsRecipe4!!["name"] as String
                    img = friendsRecipe4!!["mainIngredient"] as String
                    changeImage(recipe4image, img)
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe4layout).visibility = View.GONE
                }
                if (friendsRecipe5 != null) {
                    view.findViewById<TextView>(R.id.recipe5name).text = friendsRecipe5!!["name"] as String
                    img = friendsRecipe5!!["mainIngredient"] as String
                    changeImage(recipe5image, img)
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.VISIBLE
                } else {
                    view.findViewById<LinearLayout>(R.id.recipe5layout).visibility = View.GONE
                }
            }

            nextButton.visibility = View.VISIBLE
            prevButton.visibility = View.GONE
        }

        // For each recipe image, if the user clicks on it, then it navigates to the View Recipe page
        // and sends the necessary hashmap to the fragment result
        recipe1image.setOnClickListener() {
            if ((nextButton.visibility == View.VISIBLE) && (!friendsActive)){
                setFragmentResult("recipeKey", bundleOf("recipe1" to recipe1))
            } else if ((nextButton.visibility == View.GONE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe6" to recipe6))
            }
            if ((nextButton.visibility == View.VISIBLE) && (friendsActive)){
                setFragmentResult("recipeKey", bundleOf("recipe1" to friendsRecipe1))
            } else if ((nextButton.visibility == View.GONE) && (friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe6" to friendsRecipe6))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe2image.setOnClickListener() {
            if ((nextButton.visibility == View.VISIBLE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe2" to recipe2))
            } else if ((nextButton.visibility == View.GONE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe7" to recipe7))
            }
            if ((nextButton.visibility == View.VISIBLE) && (friendsActive)){
                setFragmentResult("recipeKey", bundleOf("recipe2" to friendsRecipe2))
            } else if ((nextButton.visibility == View.GONE) && (friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe7" to friendsRecipe7))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe3image.setOnClickListener() {
            if ((nextButton.visibility == View.VISIBLE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe3" to recipe3))
            } else if ((nextButton.visibility == View.GONE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe8" to recipe8))
            }
            if ((nextButton.visibility == View.VISIBLE) && (friendsActive)){
                setFragmentResult("recipeKey", bundleOf("recipe3" to friendsRecipe3))
            } else if ((nextButton.visibility == View.GONE) && (friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe8" to friendsRecipe8))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe4image.setOnClickListener() {
            if ((nextButton.visibility == View.VISIBLE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe4" to recipe4))
            } else if ((nextButton.visibility == View.GONE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe9" to recipe9))
            }
            if ((nextButton.visibility == View.VISIBLE) && (friendsActive)){
                setFragmentResult("recipeKey", bundleOf("recipe4" to friendsRecipe4))
            } else if ((nextButton.visibility == View.GONE) && (friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe9" to friendsRecipe9))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
        recipe5image.setOnClickListener() {
            if ((nextButton.visibility == View.VISIBLE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe5" to recipe5))
            } else if ((nextButton.visibility == View.GONE) && (!friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe10" to recipe10))
            }
            if ((nextButton.visibility == View.VISIBLE) && (friendsActive)){
                setFragmentResult("recipeKey", bundleOf("recipe5" to friendsRecipe5))
            } else if ((nextButton.visibility == View.GONE) && (friendsActive)) {
                setFragmentResult("recipeKey", bundleOf("recipe10" to friendsRecipe10))
            }
            navController.navigate(R.id.action_recipe_to_recipeView)
        }
    }
    fun resetFriendsRecipes() {
        friendsRecipe1 = null
        friendsRecipe2 = null
        friendsRecipe3 = null
        friendsRecipe4 = null
        friendsRecipe5 = null
        friendsRecipe6 = null
        friendsRecipe7 = null
        friendsRecipe8 = null
        friendsRecipe9 = null
        friendsRecipe10 = null
    }

}