package com.example.drinkmate

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import java.util.*

//ChatFragment Class represents the fragment implementation for the chat menu fragment
class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirestoreRecyclerAdapter<Message, MessageViewHolder>
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var currentUser: String
    private lateinit var friendToken: String
    private var friendUid: String = ""

    //onCreateView initialized and inflates the chat layout view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)

        //Get friend uid
        friendUid = arguments?.getString(FRIEND_UID_KEY) ?: ""

        //Get current user's token and username
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(uid!!)
        userRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                friendToken = document.getString("userToken").toString()
                val username = document.getString("userName")
                if (username != null) {
                    currentUser = username
                }
            }
        }

        //Get all messages between current user and user's friend from Firestore
        val query = FirebaseFirestore.getInstance()
            .collection("conversations")
            .document(getConversationId(FirebaseAuth.getInstance().currentUser?.uid ?: "", friendUid))
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)

        //Set up FirestoreRecyclerAdapter for displaying list of messages
        val options = FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(query, Message::class.java)
            .build()

        //Initialize and set up FirestoreRecyclerAdapter to the recyclerview in the chat layout
        adapter = object : FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
                return MessageViewHolder(view)
            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
                holder.bind(model)
            }
        }

        //Set a layout manager for the recyclerview
        val layoutManager = LinearLayoutManager(context)
        // Scroll to the bottom of the RecyclerView
        layoutManager.stackFromEnd = true;
        recyclerView.layoutManager = layoutManager
        // Initialize the adapter and set it to the RecyclerView
        recyclerView.adapter = adapter

        //Click listener for send message button to send message
        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                messageEditText.setText("")
            }
        }
        return view
    }

    //onViewCreated method for after view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Scroll RecyclerView to bottom
        recyclerView.scrollToPosition(adapter.itemCount - 1)

    }

    //Get the id of a conversation
    private fun getConversationId(uid1: String, uid2: String): String {
        return if (uid1 < uid2) "$uid1-$uid2" else "$uid2-$uid1"
    }

    //Send message from current user to friend
    private fun sendMessage(messageText: String) {
        val message = Message(FirebaseAuth.getInstance().currentUser?.uid ?: "", currentUser, friendUid, messageText, Date())

        val conversationRef = FirebaseFirestore.getInstance()
            .collection("conversations")
            .document(getConversationId(message.senderUId, message.receiverUId))

        conversationRef.collection("messages").add(message)
        recyclerView.scrollToPosition(adapter.itemCount - 1)

        // Get the device token for the recipient from your Firebase Cloud Firestore database
        val recipientToken = friendToken

        // Set up a Firebase Cloud Messaging notification message with the message content and recipient's device token
        val dropNotifcation = RemoteMessage.Builder(recipientToken)
            .setMessageId(UUID.randomUUID().toString())
            .setData(mapOf("message" to messageText))
            .build()

        // Send the message to the Firebase Cloud Messaging server
        FirebaseMessaging.getInstance().send(dropNotifcation)
    }

    //Get the friend's device token
    private fun getRecipientToken(): String {
        return friendToken
    }

    //Set the friend's device token
    private fun getRecipientToken1(recipientId: String) {
        val recipientRef = FirebaseFirestore.getInstance().collection("users").document(recipientId)
        recipientRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val recipientToken = document.getString("deviceToken")
                if (recipientToken != null) {
                    friendToken = recipientToken
                    return@addOnSuccessListener
                }
            } else {
                friendToken = ""
                return@addOnSuccessListener
            }
        }
    }

    //onStart method for after view activity is active
    override fun onStart() {
        super.onStart()
        adapter.startListening()
        // Scroll RecyclerView to bottom
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    //onStop method for after view activity ceases
    override fun onStop() {
        super.onStop()
    }

    //MessageViewHolder class implements the viewholder for holding each messaage item element in a chat menu
    private inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Initialize 3 layout views in a message: username, timestamp, message
        private val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        private val userNameChatTextView: TextView = view.findViewById(R.id.userNameChatTextView)
        private val timeTextView: TextView = view.findViewById(R.id.timeTextView)

        //Bind data to the message's username, timestamp, message textviews
        fun bind(message: Message) {
            messageTextView.text = message.content
            userNameChatTextView.text = message.senderUserName
            timeTextView.text = message.timestamp.toString()
        }
    }

    //Companion object for holding the friend uid string
    companion object {
        private const val FRIEND_UID_KEY = "FRIEND_UID_KEY"

        //Creates a new instance of the chat menu fragment based on the selected friend
        fun newInstance(friendUid: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putString(FRIEND_UID_KEY, friendUid)
            fragment.arguments = args
            return fragment
        }
    }
}
