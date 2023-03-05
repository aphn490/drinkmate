package com.example.drinkmate

import android.app.Activity
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
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

//FriendsAdapter Class to create an implementation of the FirestoreRecyclerOptions to handle population of friend elements in the list of friends
class FriendsAdapter(
    options: FirestoreRecyclerOptions<User>,
    private val listener: OnItemClickListener,
    private val activity: Activity
) : FirestoreRecyclerAdapter<User, FriendsAdapter.ViewHolder>(options) {

    //Click listener interface
    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

    //onCreateViewHolder occurs when the viewholder is set to be created, inflate the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    //onBindViewHolder binds data to the viewholder based on position
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: User) {
        holder.bind(model, listener, activity)
    }

    //Viewholder class to implement the view holder object for holding the friend elements in the friends list
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.name)

        //Bind data to the view holder the friend item elements in the friends list
        fun bind(user: User, listener: OnItemClickListener, activity: Activity) {
            nameTextView.text = user.userName
            //Listener for when the chat option for a friend is clicked on, set the view to the chat menu view
            itemView.setOnClickListener {
                listener.onItemClick(user)
                val chatFragment = ChatFragment.newInstance(user.uid)
                val fragmentTransaction = (activity as FragmentActivity).supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fc, chatFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }
}