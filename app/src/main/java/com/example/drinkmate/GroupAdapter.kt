package com.example.drinkmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter (private val groupList : ArrayList<GroupForRecycler>) :
    RecyclerView.Adapter<GroupAdapter.MyViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_group_display_layout, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = groupList[position]
        holder.groupName.text = currentItem.groupName
        holder.numUsers.text = currentItem.numberOfMembers.toString()
    }

    override fun getItemCount(): Int {
        return groupList.size
    }
    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val groupName : TextView = itemView.findViewById(R.id.GroupName)
        val numUsers : TextView = itemView.findViewById(R.id.NumberOfUsers)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }


}