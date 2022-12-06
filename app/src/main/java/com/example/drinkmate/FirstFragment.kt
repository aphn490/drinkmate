package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.drinkmate.databinding.FragmentFirstBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

data class User(val id: Int, val name: String)

class FirstFragment : Fragment() {
    val db = Firebase.firestore
    var userArray = ArrayList<String>()
    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFirst.setOnClickListener {
            userArray.clear()
            val userData:String = binding.editTextTextPersonName.getText().toString()
            var toast = Toast.makeText(activity, "Adding " + userData + " to DB.", Toast.LENGTH_LONG)
            toast.show()
            var user = hashMapOf(
                "name" to userData
            )
            db.collection("Users").add(user)
            var allnames:String = "Users: "
            val allUsers = db.collection("Users").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        allnames = allnames + document["name"].toString()
                    }
                }
            allnames = allnames + "Thomson"
            var toast2 = Toast.makeText(activity, allnames, Toast.LENGTH_LONG)
            toast2.show()
            binding.textView.text = allnames
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}