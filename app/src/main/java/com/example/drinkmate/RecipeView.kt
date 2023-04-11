package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import kotlinx.coroutines.*

// A Recipe View fragment that will handle with displaying detailed information on a recipe
class RecipeView : Fragment() {
    private var arr: HashMap<String, *>? = null
    private var details: MutableList<String> = arrayListOf()

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
        // Initialization of a job and coroutine in order to make sure a job is completed first before
        // going into code that needs the result
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        // The setFragmentResultListener will retrieve data that was sent to it with the attached
        // key, it will retrieve the arraylist from Recipe
        setFragmentResultListener("recipeKey") { key, bundle ->
            uiScope.launch(Dispatchers.IO) {
                // Assigns arr variable to the ArrayList that is retrieved
                arr = bundle.get("list") as HashMap<String, *>?
                // Assigns variables to the data in the HashMap and then adds the values to the details array list
                val name: String = arr?.get("name") as String
                val mainIngredient: String = arr?.get("mainIngredient") as String
                var ingredients: String = arr?.get("ingredients") as String
                var steps: String = arr?.get("steps") as String
                ingredients = ingredients.replace("|", "\n")
                steps = steps.replace("|", "\n")
                details.add(name)
                details.add(mainIngredient)
                details.add(ingredients)
                details.add(steps)
                withContext(Dispatchers.Main) {
                    for (x in details) {
                        System.out.println(x)
                    }
                    // Sets the text boxes to each of the index of the details, which includes name, mainIngredient
                    // ingredients, steps
                    view?.findViewById<TextView>(R.id.viewRecipeName)?.text = details[0]
                    view?.findViewById<TextView>(R.id.mainAlc)?.text = details[1]
                    view?.findViewById<TextView>(R.id.viewIngredients)?.text = details[2]
                    view?.findViewById<TextView>(R.id.viewSteps)?.text = details[3]
                    view?.let { changeImage(it.findViewById<ImageView>(R.id.viewRecipeImage), details[1]) }
                }
                }
            }
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
        returnButton.setOnClickListener() {
            activity?.onBackPressed()
        }
    }

}