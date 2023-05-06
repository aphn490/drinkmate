package com.example.drinkmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Shopping : Fragment() {
    // Initializes variables
    private val db = Firebase.firestore
    private var cart1: HashMap<String, *>? = null
    private var cart2: HashMap<String, *>? = null
    private var cart3: HashMap<String, *>? = null
    private var cart4: HashMap<String, *>? = null
    private var cart5: HashMap<String, *>? = null
    private val uid = FirebaseAuth.getInstance().uid.toString()

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
        // Initializes variables
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val navButton = view.findViewById<Button>(R.id.createShopping)
        val cart1name = view.findViewById<TextView>(R.id.cart1)
        val cart2name = view.findViewById<TextView>(R.id.cart2)
        val cart3name = view.findViewById<TextView>(R.id.cart3)
        val cart4name = view.findViewById<TextView>(R.id.cart4)
        val cart5name = view.findViewById<TextView>(R.id.cart5)

        navButton.setOnClickListener(){
            navController.navigate(R.id.action_shopping_to_shoppingCreation)
        }

        // Fetches information of shopping carts from database
        db.collection("ShoppingCarts").document(uid).get()
            .addOnSuccessListener {
                // If it exists, it'll fetch the information and assign the variable to it
                if (it.get("carts1") != null) {
                    cart1 = it.get("carts1") as HashMap<String, *>
                }
                if (it.get("carts2") != null) {
                    cart2 = it.get("carts2") as HashMap<String, *>
                }
                if (it.get("carts3") != null) {
                    cart3 = it.get("carts3") as HashMap<String, *>
                }
                if (it.get("carts4") != null) {
                    cart4 = it.get("carts4") as HashMap<String, *>
                }
                if (it.get("carts5") != null) {
                    cart5 = it.get("carts5") as HashMap<String, *>
                }
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    if (cart1 != null) {
                        cart1name.text = cart1!!["name"] as String
                    }
                    if (cart2 != null) {
                        cart2name.text = cart2!!["name"] as String
                    }
                    if (cart3 != null) {
                        cart3name.text = cart3!!["name"] as String
                    }
                    if (cart4 != null) {
                        cart4name.text = cart4!!["name"] as String
                    }
                    if (cart5 != null) {
                        cart5name.text = cart5!!["name"] as String
                    }
                }
            }
        // Navigates to the different pages if it exists
        cart1name.setOnClickListener() {
            setFragmentResult("cartKey", bundleOf("carts1" to cart1))
            navController.navigate(R.id.action_shopping_to_shoppingView)
        }
        cart2name.setOnClickListener() {
            setFragmentResult("cartKey", bundleOf("carts2" to cart2))
            navController.navigate(R.id.action_shopping_to_shoppingView)
        }
        cart3name.setOnClickListener() {
            setFragmentResult("cartKey", bundleOf("carts3" to cart3))
            navController.navigate(R.id.action_shopping_to_shoppingView)
        }
        cart4name.setOnClickListener() {
            setFragmentResult("cartKey", bundleOf("carts4" to cart4))
            navController.navigate(R.id.action_shopping_to_shoppingView)
        }
        cart5name.setOnClickListener() {
            setFragmentResult("cartKey", bundleOf("carts5" to cart5))
            navController.navigate(R.id.action_shopping_to_shoppingView)
        }
    }
}