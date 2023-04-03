package com.example.drinkmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class FindGroup : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var groupRecyclerView: RecyclerView
    private lateinit var groupArrayList: ArrayList<GroupForRecycler>
    private lateinit var myGroupAdapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_group)

        groupRecyclerView = findViewById(R.id.searchResultList)
        groupRecyclerView.layoutManager = LinearLayoutManager(this)
        groupRecyclerView.setHasFixedSize(true)

        groupArrayList = arrayListOf()
        myGroupAdapter = GroupAdapter(groupArrayList)

        groupRecyclerView.adapter = myGroupAdapter
        myGroupAdapter.setOnItemClickListener(object : GroupAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@FindGroup, GroupMainPage::class.java)
                intent.putExtra("Description", groupArrayList[position].Description)
                intent.putExtra("Name", groupArrayList[position].GroupName)
                intent.putExtra("imgsrc", groupArrayList[position].image)
                startActivity(intent)
            }
        })

        eventChangeListener()

    }

    private fun eventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("Groups")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED){
                            groupArrayList.add(dc.document.toObject(GroupForRecycler::class.java))
                        }
                    }
                    myGroupAdapter.notifyDataSetChanged()
                }
            })
    }
}