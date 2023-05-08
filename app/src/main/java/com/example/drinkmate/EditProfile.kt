package com.example.drinkmate

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class EditProfile : Fragment() {

    private lateinit var newEmail: TextInputEditText
    private lateinit var username: TextInputEditText
    private lateinit var fullName: TextInputEditText
    private lateinit var uploadButton: Button
    private lateinit var imageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var doneButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private var imagePicked: Boolean = false

    companion object{
        const val  IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        newEmail = view.findViewById(R.id.newEmail)
        username = view.findViewById(R.id.username)
        fullName = view.findViewById(R.id.fullname)
        uploadButton = view.findViewById(R.id.upldButton)
        imageView = view.findViewById(R.id.uploadImage)
        saveButton = view.findViewById(R.id.saveButton)
        doneButton = view.findViewById(R.id.doneButton)
        auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser?.uid

        //setup the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")



        //button to upload the image
        uploadButton.setOnClickListener(){
            pickImageGallery()
        }
        //button to apply changes
        saveButton.setOnClickListener(){

            //initialize the updated contents
            val em = newEmail.text.toString()
            val userN = username.text.toString()
            val full = fullName.text.toString()
            val user = User(userN, full)

            if (uid != null){

                if(em.isNotEmpty()){

                    //update the email
                    auth.currentUser!!.updateEmail(em)

                }

                //create new user in firebase storage
                databaseReference.child(uid).setValue(user).addOnCompleteListener {

                    if (it.isSuccessful) {

                        uploadProfilePic()

                    } else {


                        Toast.makeText(activity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        //button to take you back to account
        doneButton.setOnClickListener(){

            activity?.onBackPressed()

        }

        return view
    }

    //function that opens the image gallery
    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    //function that updates the image on the editprofile page
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageUri = data?.data
            imageView.setImageURI(data?.data)
            imagePicked = true
        }
    }

    //function that updates the profile picture in firebase storage
    private fun uploadProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri!!).addOnSuccessListener {

            Toast.makeText(activity, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{

            Toast.makeText(activity, "Failed to upload the image", Toast.LENGTH_SHORT).show()

        }
    }

}


