package com.example.drinkmate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

// Fragment for the Congestion / Heat Map of bars
class Congestion : Fragment(){
    // Initializes 5 possible bars that the user can occupy by favoriting.
    private var bar1: HashMap<String, *>? = null
    private var bar2: HashMap<String, *>? = null
    private var bar3: HashMap<String, *>? = null
    private var bar4: HashMap<String, *>? = null
    private var bar5: HashMap<String, *>? = null
    private var arr: MutableList<HashMap<String, *>> = ArrayList()
    //Initializes a connection to the database
    private val db = Firebase.firestore
    private var currentuid = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_congestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Creates the navController that will let the user navigate to CongestionMap fragment.
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        // Fetches information from the database
        val favBars = db.collection("FavoriteBars").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnSuccessListener {
                // Once the fetching is done, this will assign the data that's retrieved into the variables that were created earlier
                if (it.get("bar1") != null) {
                    bar1 = it.get("bar1") as HashMap<String, *>
                }
                if (it.get("bar2") != null) {
                    bar2 = it.get("bar2") as HashMap<String, *>
                }
                if (it.get("bar3") != null) {
                    bar3 = it.get("bar3") as HashMap<String, *>
                }
                if (it.get("bar4") != null) {
                    bar4 = it.get("bar4") as HashMap<String, *>
                }
                if (it.get("bar5") != null) {
                    bar5 = it.get("bar5") as HashMap<String, *>
                }
            }.addOnCompleteListener {
                // Once the assigning of data is done, it will proceed to go on to this code.
                if (it.isSuccessful) {
                    // Checks to see if bar1 exists
                    if (bar1 != null) {
                        // Assigns variables to the design aspects that will be changed
                        val bar1image = view.findViewById<ImageView>(R.id.bar1image)
                        val bar1text = view.findViewById<TextView>(R.id.bar1desc)
                        // Loads an image from the bar into the ImageView that was just set
                        Picasso.get().load(bar1!!["image"] as String).into(bar1image)
                        // Sets the TextView description to the name that was retrieved from db
                        bar1text.text = bar1!!["name"] as String
                        // Adds the bar to the arr mutable list to be shared later
                        arr.add(bar1!!)
                        // Makes the layout that has the bar image + name visible
                        view.findViewById<LinearLayout>(R.id.bar1layout).visibility = VISIBLE
                        bar1image.setOnClickListener() {
                            confirm(bar1!!, view.findViewById(R.id.bar1layout), "bar1")
                        }
                    } else {
                        // Hides the layout that has the bar image + name just in case doesn't have 5 favorite bars
                        view.findViewById<LinearLayout>(R.id.bar1layout).visibility = GONE
                    }
                    if (bar2 != null) {
                        // Assigns variables to design aspects
                        val bar2image = view.findViewById<ImageView>(R.id.bar2image)
                        val bar2text = view.findViewById<TextView>(R.id.bar2desc)
                        // Loads image and text from db
                        Picasso.get().load(bar2!!["image"] as String).into(bar2image)
                        bar2text.text = bar2!!["name"] as String
                        arr.add(bar2!!)
                        view.findViewById<LinearLayout>(R.id.bar2layout).visibility = VISIBLE
                        bar2image.setOnClickListener() {
                            confirm(bar2!!, view.findViewById(R.id.bar2layout), "bar2")
                        }
                    } else {
                        view.findViewById<LinearLayout>(R.id.bar2layout).visibility = GONE
                    }
                    if (bar3 != null) {
                        // Assigns variables to design aspects
                        val bar3image = view.findViewById<ImageView>(R.id.bar3image)
                        val bar3text = view.findViewById<TextView>(R.id.bar3desc)
                        // Loads image and text from db
                        Picasso.get().load(bar3!!["image"] as String).into(bar3image)
                        bar3text.text = bar3!!["name"] as String
                        arr.add(bar3!!)
                        view.findViewById<LinearLayout>(R.id.bar3layout).visibility = VISIBLE
                        bar3image.setOnClickListener() {
                            confirm(bar3!!, view.findViewById(R.id.bar3layout), "bar3")
                        }
                    } else {
                        view.findViewById<LinearLayout>(R.id.bar3layout).visibility = GONE
                    }
                    if (bar4 != null) {
                        // Assigns variables to design aspects
                        val bar4image = view.findViewById<ImageView>(R.id.bar4image)
                        val bar4text = view.findViewById<TextView>(R.id.bar4desc)
                        // Loads image and text from db
                        Picasso.get().load(bar4!!["image"] as String).into(bar4image)
                        bar4text.text = bar4!!["name"] as String
                        arr.add(bar4!!)
                        view.findViewById<LinearLayout>(R.id.bar4layout).visibility = VISIBLE
                        bar4image.setOnClickListener() {
                            confirm(bar4!!, view.findViewById(R.id.bar4layout), "bar4")
                        }
                    } else {
                        view.findViewById<LinearLayout>(R.id.bar4layout).visibility = GONE
                    }
                    if (bar5 != null) {
                        // Assigns variables to design aspects
                        val bar5image = view.findViewById<ImageView>(R.id.bar5image)
                        val bar5text = view.findViewById<TextView>(R.id.bar5desc)
                        // Loads image and text from db
                        Picasso.get().load(bar5!!["image"] as String).into(bar5image)
                        bar5text.text = bar5!!["name"] as String
                        arr.add(bar5!!)
                        view.findViewById<LinearLayout>(R.id.bar5layout).visibility = VISIBLE
                        bar5image.setOnClickListener() {
                            confirm(bar5!!, view.findViewById(R.id.bar5layout), "bar5")
                        }
                    } else {
                        view.findViewById<LinearLayout>(R.id.bar5layout).visibility = GONE
                    }
                }
            }
        val button = view?.findViewById<Button>(R.id.trafficButton)
        button?.setOnClickListener(){
            // When the button is clicked, it proceeds to share the arr array list which contains
            // the data retrieved from the DB, of the user's favorite bars, with the CongestionMap
            // fragment.
            if (arr.isNotEmpty()) {
                setFragmentResult("requestKey", bundleOf("list" to arr))
                // Navigates to the Congestion Map fragment.
                navController.navigate(R.id.action_congestion_to_congestionMap)
            } else {
                Toast.makeText(requireContext(), "You have no favorite bars. Add them from locate bar feature.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    // Function to bring up the deletion confirmation window
    fun confirm(hmap : HashMap<String, *>, layout: LinearLayout, barDel : String) {
        // Initializes AlertDialog and inflates the view with the confirmation xml file
        val dialogBuilder = AlertDialog.Builder(requireContext()).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_confirmation, null)
        // Sets the view of the alertdialog to the xml and makes it visible to the user
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
        val confirmYes = dialogView.findViewById<Button>(R.id.confirmYes)
        confirmYes.setOnClickListener() {
            // Upon hitting the Yes button, the app will now fetch the document under FavoriteBars with the
            // same uid as the logged in user
            val doc = db.collection("FavoriteBars").document(currentuid.toString())
            val update = hashMapOf<String, Any> (
                        // It will delete the field of the bar, based on the string that's passed to the function
                        // Examples: "bar1", "bar3" are deleted
                        barDel to FieldValue.delete()
                    )
            // Updates the firebase document with the same fields but specific fields are removed
            doc.update(update)
            // Removes the deleted bar from the array list so it's not sent to congestion_map
            arr.remove(hmap!!)
            layout.visibility = GONE
            dialogBuilder.dismiss()
        }
        val confirmNo = dialogView.findViewById<Button>(R.id.confirmNo)
        confirmNo.setOnClickListener() {
            // Upon hitting the No button, the app will dismiss the alertdialog.
            dialogBuilder.dismiss()
        }
    }

}