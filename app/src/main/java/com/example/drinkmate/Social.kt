package com.example.drinkmate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Social.newInstance] factory method to
 * create an instance of this fragment.
 */
class Social : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //
        val socialView = inflater.inflate(R.layout.fragment_social, container, false)
        /*
        val mapButton = socialView.findViewById<Button>(R.id.button_toMap)
        val friendsButton = socialView.findViewById<Button>(R.id.button_toFriendsChat)
        val friendMapButton = socialView.findViewById<Button>(R.id.button_toFriendMap)

        //Create a click listener for the map button and implement action, navigates to bar map when clicked
        mapButton.setOnClickListener {
            findNavController().navigate(R.id.action_social_to_mapsActivity)
        }

        friendsButton.setOnClickListener {
            findNavController().navigate(R.id.action_social_to_friendsChatTab2)
        }

        friendMapButton.setOnClickListener {
            findNavController().navigate(R.id.action_social_to_friendMapFragment)
        }

         */

        // Creates a value that references the friends button
        val friendsButton : TextView = socialView.findViewById(R.id.SocialFriendsTitle)
        friendsButton.setOnClickListener() {
            // Navigates the fragment to the friends fragment
            findNavController().navigate(R.id.action_social_to_friendsChatTab2)
        }
        val friendsButton2 : TextView = socialView.findViewById(R.id.SocialFriendsDesc)
        friendsButton2.setOnClickListener() {
            // Navigates the fragment to the friends fragment
            findNavController().navigate(R.id.action_social_to_friendsChatTab2)
        }

        // Creates a value that references the friend map button
        val friendMapButton : TextView = socialView.findViewById(R.id.SocialFriendMapTitle)
        friendMapButton.setOnClickListener() {
            // Navigates the fragment to the friend map fragment
            findNavController().navigate(R.id.action_social_to_friendMapFragment)
        }
        val friendMapButton2 : TextView = socialView.findViewById(R.id.SocialFriendMapDesc)
        friendMapButton2.setOnClickListener() {
            // Navigates the fragment to the friend map fragment
            findNavController().navigate(R.id.action_social_to_friendMapFragment)
        }

        // Creates a value that references the bar map button
        val barMapButton : TextView = socialView.findViewById(R.id.SocialBarMapTitle)
        barMapButton.setOnClickListener() {
            // Navigates the fragment to the bar map fragment
            findNavController().navigate(R.id.action_social_to_mapsActivity)
        }
        val barMapButton2 : TextView = socialView.findViewById(R.id.SocialBarMapDesc)
        barMapButton2.setOnClickListener() {
            // Navigates the fragment to the bar map fragment
            findNavController().navigate(R.id.action_social_to_mapsActivity)
        }

        // Inflate the layout for this fragment
        return socialView
        //return inflater.inflate(R.layout.fragment_social, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SocialFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Social().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}