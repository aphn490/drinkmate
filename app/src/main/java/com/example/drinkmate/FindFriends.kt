package com.example.drinkmate

import android.app.Person
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FindFriends : AppCompatActivity() {

    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var db : FirebaseFirestore
    private lateinit var userArrayList: ArrayList<UserForRecycler>
    private lateinit var myUserAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friends)

        searchRecyclerView = findViewById(R.id.searchResultList)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()
        myUserAdapter = UserAdapter(userArrayList)

        searchRecyclerView.adapter = myUserAdapter

        myUserAdapter.setOnItemClickListener(object : UserAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@FindFriends,PersonAccount::class.java)
                intent.putExtra("UID", userArrayList[position].UID)
                intent.putExtra("email", userArrayList[position].email)
                startActivity(intent)
                //Toast.makeText(this@FindFriends, "CURRENT USER: " + auth.currentUser?.uid.toString(), Toast.LENGTH_SHORT).show()

            }

        })

        eventChangeListener()

    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("UserAccounts")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userArrayList.add(dc.document.toObject(UserForRecycler::class.java))
                        }
                    }

                    myUserAdapter.notifyDataSetChanged()

                }

            })

    }


}