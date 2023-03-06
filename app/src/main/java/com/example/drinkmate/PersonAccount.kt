package com.example.drinkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PersonAccount : AppCompatActivity() {

    private lateinit var sendFriendRequestButton: Button
    private lateinit var declineFriendRequestButton: Button
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var CURRENT_STATE : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize all fields
        setContentView(R.layout.activity_person_account)
        val viewAccName : TextView = findViewById(R.id.accName)
        val bundle : Bundle?= intent.extras
        //data passed from previous screen
        val email = bundle!!.getString("email")
        viewAccName.text = email
        sendFriendRequestButton = findViewById(R.id.sendFriendReq)
        declineFriendRequestButton = findViewById(R.id.declineFriendReq)
        auth = FirebaseAuth.getInstance()
        val receiverUID = bundle!!.getString("UID")
        val senderID = auth.currentUser?.uid.toString()
        CURRENT_STATE = "not_friends"

        //default is invisible because we need someone to send a friend request first.
        declineFriendRequestButton.visibility = View.INVISIBLE
        declineFriendRequestButton.isEnabled = false

        //prevents users to send friend requests to themselves or spamming db with multiple requests to the same person
        if (senderID != receiverUID) {
            sendFriendRequestButton.setOnClickListener{
                sendFriendRequestButton.isEnabled = false

                if (CURRENT_STATE == "not_friends"){
                    SendFriendRequestToReceiver()
                }
                if (CURRENT_STATE == "request_sent"){
                    CancelFriendRequest()
                }
                if (CURRENT_STATE == "request_received"){
                    AcceptFriendRequest()
                }
            }
            declineFriendRequestButton.setOnClickListener{
                if (CURRENT_STATE == "request_sent" || CURRENT_STATE == "friends"){
                    RemoveOrDeclineFriend()
                }
            }

        } else {
            sendFriendRequestButton.visibility = View.INVISIBLE
            declineFriendRequestButton.visibility = View.INVISIBLE
        }

        MaintainButtons()




    }

    /**
     * Method to ensure the correct buttons are displayed when clicking an account
     */
    private fun MaintainButtons(){
        auth = FirebaseAuth.getInstance()
        val senderID = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val receiverUID = bundle!!.getString("UID")
        db = FirebaseFirestore.getInstance()
        if (receiverUID != null) {
            val docRef = db.collection("UserAccounts").document(senderID).collection("friends").document(receiverUID)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val status = documentSnapshot.data?.get("status")
                if (status == "Sent Friend Request") {
                    CURRENT_STATE = "request_sent"
                    sendFriendRequestButton.text = "Cancel Friend Request"
                    declineFriendRequestButton.visibility = View.INVISIBLE
                    declineFriendRequestButton.isEnabled = false
                } else if (status == "Pending Friend Request") {
                    CURRENT_STATE = "request_received"
                    sendFriendRequestButton.text = "Accept Friend Request"
                    declineFriendRequestButton.visibility = View.VISIBLE
                    declineFriendRequestButton.isEnabled = true
                } else if (status == "Friends") {
                    sendFriendRequestButton.isEnabled = false
                    sendFriendRequestButton.visibility = View.INVISIBLE

                    declineFriendRequestButton.text = "Remove Friend"
                    declineFriendRequestButton.isEnabled = true
                    declineFriendRequestButton.visibility = View.VISIBLE
                }
            }
        }
        if (CURRENT_STATE == "not_friends") {
            sendFriendRequestButton.visibility = View.VISIBLE
            sendFriendRequestButton.isEnabled = true
            sendFriendRequestButton.text = "Send Friend Request"
            declineFriendRequestButton.visibility = View.INVISIBLE
            declineFriendRequestButton.isEnabled = false
        }
    }

    /**
     * Method to send a friend request to a user in the DB.
     * Writes into DB into subcollection friends with receivers information
     */
    private fun SendFriendRequestToReceiver() {
        auth = FirebaseAuth.getInstance()
        val senderID = auth.currentUser?.uid.toString()
        val senderEmail = auth.currentUser?.email.toString()
        val bundle : Bundle?= intent.extras
        val receiverUID = bundle!!.getString("UID")
        val email = bundle!!.getString("email")
        db = FirebaseFirestore.getInstance()
        if (receiverUID != null) {
            val friend = hashMapOf(
                "UID" to receiverUID,
                "email" to email,
                "status" to "Sent Friend Request"
            )
            val sender = hashMapOf(
                "UID" to senderID,
                "email" to senderEmail,
                "status" to "Pending Friend Request"
            )
            db.collection("UserAccounts").document(senderID).collection("friends").document(receiverUID).set(friend)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                       db.collection("UserAccounts").document(receiverUID).collection("friends").document(senderID).set(sender)
                           .addOnCompleteListener { task2 ->
                               if (task2.isSuccessful) {
                                   sendFriendRequestButton.isEnabled = true
                                   CURRENT_STATE = "request_sent"
                                   sendFriendRequestButton.text = "Cancel Friend Request"
                                   declineFriendRequestButton.visibility = View.INVISIBLE
                                   declineFriendRequestButton.isEnabled = false
                                   Toast.makeText(this@PersonAccount, "Friend Request Sent", Toast.LENGTH_SHORT).show()
                               }
                           }
                    }
                }
        }
    }

    /**
     * Method to cancel a friend request.
     * Deletes document from "friends" subcollection in "UserAccounts" collection
     */
    private fun CancelFriendRequest(){
        auth = FirebaseAuth.getInstance()
        val senderID = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val receiverUID = bundle!!.getString("UID")
        db = FirebaseFirestore.getInstance()
        if (receiverUID != null) {
            db.collection("UserAccounts").document(senderID).collection("friends").document(receiverUID).delete()
            db.collection("UserAccounts").document(receiverUID).collection("friends").document(senderID).delete()
            CURRENT_STATE = "not_friends"
            sendFriendRequestButton.isEnabled = true
            sendFriendRequestButton.text = "Send Friend Request"
            declineFriendRequestButton.visibility = View.INVISIBLE
            declineFriendRequestButton.isEnabled = false
        }
    }

    /**
     * Method to accept a friend request from a sender
     * Changes document from "friends" subcollection in "UserAccounts" collection status -> Friends
     */
    private fun AcceptFriendRequest(){
        auth = FirebaseAuth.getInstance()
        val senderID = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val receiverUID = bundle!!.getString("UID")
        db = FirebaseFirestore.getInstance()
        if (receiverUID != null) {
            db.collection("UserAccounts").document(senderID).collection("friends")
                .document(receiverUID).update("status", "Friends")
            db.collection("UserAccounts").document(receiverUID).collection("friends")
                .document(senderID).update("status", "Friends")
            Toast.makeText(this@PersonAccount, "Friend Accepted", Toast.LENGTH_SHORT).show()
            CURRENT_STATE = "friends"
            sendFriendRequestButton.isEnabled = false
            sendFriendRequestButton.visibility = View.INVISIBLE
            declineFriendRequestButton.text = "Remove Friend"
            declineFriendRequestButton.isEnabled = true
        }
    }

    /**
     * Method for removing/declining friend
     * deletes their respective documents in the "friends" subcollection in the db
     */
    private fun RemoveOrDeclineFriend(){
        CURRENT_STATE = "not_friends"
        auth = FirebaseAuth.getInstance()
        val senderID = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val receiverUID = bundle!!.getString("UID")
        db = FirebaseFirestore.getInstance()
        if (receiverUID != null) {
            db.collection("UserAccounts").document(senderID).collection("friends")
                .document(receiverUID).delete()
            db.collection("UserAccounts").document(receiverUID).collection("friends")
                .document(senderID).delete()
        }
        declineFriendRequestButton.text = "Decline Friend Request"
        declineFriendRequestButton.isEnabled = false
        declineFriendRequestButton.visibility = View.INVISIBLE
        sendFriendRequestButton.text = "Send Friend Request"
        sendFriendRequestButton.visibility = View.VISIBLE
        sendFriendRequestButton.isEnabled = true

    }
}