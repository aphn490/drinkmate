package com.example.drinkmate

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [Account.newInstance] factory method to
 * create an instance of this fragment.
 */
class Account : Fragment() {

    //Lines 40-42, 55-58, 66-116 done by Jedidiah Shank
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private lateinit var modeSwitch: SwitchCompat

    private var nightMode:Boolean=false

    private var editor:SharedPreferences.Editor?=null

    private var sharedPreferences:SharedPreferences?=null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val accountview = inflater.inflate(R.layout.fragment_account, container, false)
        val findFriendsButton = accountview.findViewById<Button>(R.id.findFriends)

        //initialize and connect variable user contents to specified locations
        var currentEmail = accountview.findViewById<TextView>(R.id.accEmail)
        var userN = accountview.findViewById<TextView>(R.id.accUsername)
        var fName = accountview.findViewById<TextView>(R.id.accFullName)
        var profilePic = accountview.findViewById<ImageView>(R.id.profilePicture)

        modeSwitch = accountview.findViewById(R.id.mode_switch)

        sharedPreferences = getActivity()?.getSharedPreferences("MODE", Context.MODE_PRIVATE)

        nightMode = sharedPreferences?.getBoolean("night", false)!!

        if(nightMode)
        {
            modeSwitch.isChecked = true

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        modeSwitch.setOnCheckedChangeListener { compoundButton, state ->

            if(nightMode)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                editor = sharedPreferences?.edit()

                editor?.putBoolean("night", false)
            }
            else{

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                editor = sharedPreferences?.edit()

                editor?.putBoolean("night", true)

            }

            editor?.apply()

        }


        findFriendsButton.setOnClickListener {
            findNavController().navigate(R.id.action_account_to_find_friends)
        }

        //connect edit button to editProfile page
        val editButton : Button = accountview.findViewById(R.id.editProfile)
        editButton.setOnClickListener{
            findNavController().navigate(R.id.action_account_to_edit_profile)
        }
        val findGroupButton = accountview.findViewById<Button>(R.id.findGroup)
        findGroupButton.setOnClickListener{
            findNavController().navigate(R.id.action_more_to_find_group)
        }
        auth = FirebaseAuth.getInstance()


        val uid = auth.currentUser?.uid

        //input user email into email slot
        currentEmail.text = FirebaseAuth.getInstance().currentUser?.email

        //navigate through firebase to find the deposit of the "user" class
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(uid!!).get().addOnCompleteListener {

            if(it.result.exists()){

                if(it.result.exists()){

                    val dataSnapshot: DataSnapshot = it.result

                    //input user info to display on account page
                    userN.text = dataSnapshot.child("username").value as CharSequence?
                    fName.text = dataSnapshot.child("fullname").value as CharSequence?


                }

            }

        }


        var imageID: String = auth.currentUser?.uid.toString()

        //locate image on firebase by using user uid and store image into a temporary file
        storageReference = FirebaseStorage.getInstance().getReference("Users/$imageID")
        var localFile: File = File.createTempFile("tempfile",".jpg")
        storageReference.getFile(localFile).addOnSuccessListener {

            //change image uri to saved photo if one is found
            var imageUri: Uri = Uri.parse(localFile.toString())
            profilePic.setImageURI(imageUri)

        }.addOnFailureListener{

            //use default icon if no image is found
            var defaultUri: Uri = Uri.parse("android.resource://com.example.drinkmate/${R.drawable.icon}")
            profilePic.setImageURI(defaultUri)

        }





        return accountview
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Account().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}