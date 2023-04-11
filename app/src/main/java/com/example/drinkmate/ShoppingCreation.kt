package com.example.drinkmate

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Shopping Creation fragment that will allow you to create a shopping cart by adding ingredients to them.
class ShoppingCreation : Fragment() {
    // Initializes database and retrieves current user id
    private val db = Firebase.firestore
    private var currentuid = FirebaseAuth.getInstance().currentUser?.uid
    private var cart : HashMap<String, String> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Initializes all the buttons and edit text references
        val addButton = view.findViewById<Button>(R.id.addShoppingItem)
        val saveButton = view.findViewById<Button>(R.id.shoppingSave)
        val shoppingItem = view.findViewById<EditText>(R.id.shoppingCartItem)
        val shoppingAmount = view?.findViewById<EditText>(R.id.shoppingCartAmount)
        val recipeRef = db.collection("ShoppingCarts").document(currentuid.toString())
        addButton.setOnClickListener() {
            if (shoppingItem?.text.toString() != "" && shoppingAmount?.text.toString() != "") {
                val amount = shoppingAmount?.text.toString()
                val item = shoppingItem?.text.toString()
                cart[item] = amount
                shoppingItem?.setText("")
                shoppingAmount?.setText("")
            }
        }
        saveButton.setOnClickListener() {
            recipeRef.get().addOnCompleteListener {
                    task ->
                //Once the task is completed, it'll undergo the following code
                if (task.isSuccessful) {
                    val doc = task.result
                    // If cart 1 doesn't exist yet, it will update the document with the bar details under
                    // the name "carts"
                    if (doc.get("carts1") == null) {
                        val cartCreated = hashMapOf (
                            "carts1" to cart
                        )
                        db.collection("ShoppingCarts").document(currentuid.toString()).set(cartCreated, SetOptions.merge())
                        // If cart 2 doesn't exist yet, it will update the document with the bar details under
                        // the name "carts"
                    } else if (doc.get("carts2") == null) {
                        val cartCreated = hashMapOf (
                            "carts2" to cart
                        )
                        db.collection("ShoppingCarts").document(currentuid.toString()).set(cartCreated, SetOptions.merge())
                        // If cart 3 doesn't exist yet, it will update the document with the bar details under
                        // the name "carts"
                    } else if (doc.get("carts3") == null) {
                        val cartCreated = hashMapOf (
                            "carts3" to cart
                        )
                        db.collection("ShoppingCarts").document(currentuid.toString()).set(cartCreated, SetOptions.merge())
                        // If cart 4 doesn't exist yet, it will update the document with the bar details under
                        // the name "carts"
                    } else if (doc.get("carts4") == null) {
                        val cartCreated = hashMapOf (
                            "carts4" to cart
                        )
                        db.collection("ShoppingCarts").document(currentuid.toString()).set(cartCreated, SetOptions.merge())
                        // If cart 5 doesn't exist yet, it will update the document with the bar details under
                        // the name "carts"
                    } else if (doc.get("carts5") == null) {
                        val cartCreated = hashMapOf (
                            "carts5" to cart
                        )
                        db.collection("ShoppingCarts").document(currentuid.toString()).set(cartCreated, SetOptions.merge())
                        // If recipe 6 doesn't exist yet, it will update the document with the bar details under
                        // the name "recipe6"
                    } else {
                        Toast.makeText(requireContext(), "Max Recipes reached.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(ContentValues.TAG, "get failed with ", task.exception)
                }
            }
        }
    }
}