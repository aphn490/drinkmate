package com.example.drinkmate

import android.content.Context
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

//FriendsList Class represents the fragment implementation for the friends list
class FriendsList : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirestoreRecyclerAdapter<User, FriendViewHolder>

    //onCreateView occurs when the viewholder is created, inflate the view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends_list, container, false)

        recyclerView = view.findViewById(R.id.rvFriends)

        /*
        //OLD: Get a reference to the friends collection of each user in users collection
        val query = FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .collection("friends")
         */

        //OLD: Get a reference to the friends collection of each user in users collection
        val query = FirebaseFirestore.getInstance()
            .collection("UserAccounts")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .collection("friends")

        //Adapter options set to the query made to the list of friends
        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        //Initialize RecyclerView RecyclerView
        adapter = object : FirestoreRecyclerAdapter<User, FriendViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
                val friendsListView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
                return FriendViewHolder(friendsListView)
            }

            override fun onBindViewHolder(holder: FriendViewHolder, position: Int, model: User) {
                holder.bind(model)
            }
        }

        recyclerView.adapter = adapter
        return view
    }

    //onStart method for after view activity is active
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    //onStop method for after view activity ceases
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    //FriendViewholder class to implement the view holder object for holding the friend elements in the friends list
    private inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profileImageView: ImageView = view.findViewById(R.id.profile_image)
        private val friendNameTextView: TextView = view.findViewById(R.id.name)
        private val chatButton: ImageButton= view.findViewById(R.id.chat_button)

        // Bind the data to the views in the item layout
        fun bind(user: User) {
            /*
            Glide.with(itemView.context)
                .load(user.profileImageUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(profileImageView)
             */

            val fsdb = FirebaseFirestore.getInstance()
            val usersCollectionRef = fsdb.collection("UserAccounts")
            val userRef = usersCollectionRef.document(user.UID)
            userRef.get().addOnSuccessListener { documentSnapshot ->
                val field = documentSnapshot.getString("userName")
                friendNameTextView.text = field
            }.addOnFailureListener { exception ->
                // handle the exception here
            }

            //Create a click listener for the map button and implement action, navigates to bar map when clicked
            chatButton.setOnClickListener {
                // Handle opening a chat with the friend
                val friendId = user.UID // or user.documentId, depending on how you store the IDs
                val chatFragment = ChatFragment.newInstance(friendId)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fc, chatFragment)
                    .addToBackStack(null)
                    .commit()
            }

        }
    }
}

