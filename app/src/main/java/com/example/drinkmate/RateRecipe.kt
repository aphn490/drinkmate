package com.example.drinkmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

class RateRecipe : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rate_recipe, container, false)
        //creating variables
        var db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var stars: RatingBar
        var send: Button
        var feedback: EditText

        feedback = view.findViewById(R.id.userFeedback)
        send = view.findViewById(R.id.rateButton)
        stars = view.findViewById(R.id.stars)

        //sends rating to database
        send.setOnClickListener{
            val starRating = stars.rating.toString()
            val userFeedback = feedback.text.toString()
            val rFeedback = RatingFeedback(starRating, userFeedback)

            if (starRating.isNotEmpty() && userFeedback.isNotEmpty()){
                db.collection("RecipeRating").add(rFeedback)
                Toast.makeText(activity, "Rating has been made",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity, "Please fill out the fields",Toast.LENGTH_SHORT).show()
            }

            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
            val userDocumentRef = FirebaseFirestore.getInstance().collection("UserAccounts").document(currentUserID ?: "")
            userDocumentRef?.update("num_recipes_rated", FieldValue.increment(1))
        }
        return view
    }
}

data class RatingFeedback(
    var star: String? = null,
    var feedback: String? = null
)