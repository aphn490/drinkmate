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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_users_display_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.UID.text = currentItem.UID
        holder.em.text = currentItem.email

    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val UID : TextView = itemView.findViewById(R.id.userID)
        val em : TextView = itemView.findViewById(R.id.userEmail)

    }

}