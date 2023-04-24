package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

// A Recipe View fragment that will handle with displaying detailed information on a recipe
class RecipeView : Fragment() {
    private var arr: HashMap<String, *>? = null
    private var details: MutableList<String> = arrayListOf()
    private val uid = FirebaseAuth.getInstance().uid.toString()
    private var recipe: HashMap<String, *>? = null
    private val db = Firebase.firestore
    private var allNotes : MutableList<String> = ArrayList()
    private var recipeNumber = ""
    // Function to set the image based on the main ingredient chosen
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val returnButton = view.findViewById<Button>(R.id.viewReturn)
        // Initialization of a job and coroutine in order to make sure a job is completed first before
        // going into code that needs the result
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        val notesDesc = view.findViewById<TextView>(R.id.viewNotesDesc)
        val stepsDesc = view.findViewById<TextView>(R.id.viewStepsDesc)
        val ingDesc = view.findViewById<TextView>(R.id.viewIngredientsDesc)
        val notesContent = view.findViewById<TextView>(R.id.viewNotes)
        val stepsContent = view.findViewById<TextView>(R.id.viewSteps)
        val ingContent = view.findViewById<TextView>(R.id.viewIngredients)
        val showNotes = view.findViewById<Button>(R.id.showNotes)
        val hideNotes = view.findViewById<Button>(R.id.notesReturn)
        val notesLayout = view.findViewById<LinearLayout>(R.id.addNotesLayout)
        val addNotes = view.findViewById<Button>(R.id.addNotes)
        val removeNotes = view.findViewById<Button>(R.id.removeNotes)
        val postNotes = view.findViewById<Button>(R.id.postNotes)
        val notesInput = view.findViewById<EditText>(R.id.notesList)
        var allNotesString = ""
        var email = ""

        db.collection("UserAccounts").document(uid).get()
            .addOnSuccessListener {
                email = it.get("email").toString()
            }
        notesLayout.visibility = View.GONE
        notesContent.visibility = View.GONE
        notesDesc.visibility = View.GONE
        showNotes.visibility = View.GONE
        hideNotes.visibility = View.GONE
        postNotes.visibility = View.GONE
        // The setFragmentResultListener will retrieve data that was sent to it with the attached
        // key, it will retrieve the arraylist from Recipe
        setFragmentResultListener("recipeKey") { key, bundle ->
            uiScope.launch(Dispatchers.IO) {
                // Assigns arr variable to the ArrayList that is retrieved
                for (x in bundle.keySet()) {
                    recipeNumber = x.toString()
                }
                arr = bundle.get(recipeNumber) as HashMap<String, HashMap<String, *>?>
                // Assigns variables to the data in the HashMap and then adds the values to the details array list
                val name: String = arr?.get("name") as String
                val mainIngredient: String = arr?.get("mainIngredient") as String
                var ingredients: String = arr?.get("ingredients") as String
                var steps: String = arr?.get("steps") as String
                val id: String = arr?.get("creator") as String
                var notes: String = arr?.get("notes") as String
                var finalnotes = notes.replace("|", "\n")
                ingredients = ingredients.replace("|", "\n")
                steps = steps.replace("|", "\n")
                details.add(name)
                details.add(mainIngredient)
                details.add(ingredients)
                details.add(steps)
                details.add(finalnotes)
                withContext(Dispatchers.Main) {
                    recipe = arr
                    // Sets the text boxes to each of the index of the details, which includes name, mainIngredient
                    // ingredients, steps
                    if (id == uid) {
                        showNotes?.visibility = View.GONE
                    } else {
                        showNotes?.visibility = View.VISIBLE

                    }
                    view.findViewById<TextView>(R.id.viewRecipeName).text = details[0]
                    view.findViewById<TextView>(R.id.mainAlc).text = details[1]
                    ingContent.text = details[2]
                    stepsContent.text = details[3]
                    view.let { changeImage(it.findViewById<ImageView>(R.id.viewRecipeImage), details[1]) }
                    notesContent.text = details[4]
                    showNotes.setOnClickListener{
                        showNotes.visibility = View.GONE
                        stepsDesc.visibility = View.GONE
                        stepsContent.visibility = View.GONE
                        ingDesc.visibility = View.GONE
                        ingContent.visibility = View.GONE
                        notesContent.visibility = View.VISIBLE
                        notesDesc.visibility = View.VISIBLE
                        hideNotes.visibility = View.VISIBLE
                        notesLayout.visibility = View.VISIBLE
                        postNotes.visibility = View.VISIBLE
                    }
                    hideNotes.setOnClickListener {
                        showNotes.visibility = View.VISIBLE
                        stepsDesc.visibility = View.VISIBLE
                        stepsContent.visibility = View.VISIBLE
                        ingDesc.visibility = View.VISIBLE
                        ingContent.visibility = View.VISIBLE
                        notesContent.visibility = View.GONE
                        notesDesc.visibility = View.GONE
                        hideNotes.visibility = View.GONE
                        notesLayout.visibility = View.GONE
                        postNotes.visibility = View.GONE
                    }
                    addNotes.setOnClickListener {
                        if (notesInput.text.toString() != "") {
                            // Adds the step to a list of steps and makes a toast
                            allNotes.add(notesInput.text.toString())
                            Toast.makeText(requireContext(), "Added " + notesInput.text.toString() + " to Notes.", Toast.LENGTH_SHORT).show()
                            notesInput.setText("")
                        }
                    }

                    removeNotes.setOnClickListener {
                        if(allNotes.isNotEmpty()) {
                            // Removes the ingredient at last index - 1
                            Toast.makeText(requireContext(), "Removed " + allNotes.get(allNotes.size - 1) + " from Notes.", Toast.LENGTH_SHORT).show()
                            allNotes.removeAt(allNotes.size - 1)
                        } else {
                            Toast.makeText(requireContext(), "No Notes Added Yet.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    postNotes.setOnClickListener {
                        allNotesString = email + ": "
                        for (x in allNotes) {
                            allNotesString = allNotesString + x + ". "
                        }
                        allNotesString += "|"
                        notes += allNotesString
                        val docRef = db.collection("Recipes").document(id)
                        docRef.update("$recipeNumber.notes", notes)
                        finalnotes = notes.replace("|", "\n")
                        notesContent.text = finalnotes
                    }
                }
            }
        }
        returnButton.setOnClickListener() {
            activity?.onBackPressed()
        }
    }

}