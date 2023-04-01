package com.example.drinkmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import org.w3c.dom.Text

class UserAdapter(private val userList : ArrayList<UserForRecycler>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener

    /**
     * interface that allows us to index a user from the recyclerview
     */
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    /**
     * allows us to click on an item in the recylcerview
     */
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    /**
     * Required Method
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_users_display_layout, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    /**
     * Required Method
     */
    override fun getItemCount(): Int {
        return userList.size
    }

    /**
     * fills in the fields with the correct data from the DB
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.UID.text = currentItem.UID
        holder.em.text = currentItem.email

    }

    /**
     * Allows entries to be clicked on.
     */
    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val UID : TextView = itemView.findViewById(R.id.userID)
        val em : TextView = itemView.findViewById(R.id.userEmail)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

}