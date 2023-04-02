package com.example.drinkmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class GroupMainPage : AppCompatActivity() {

    private lateinit var interactButton: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var allGroupMembers: RecyclerView
    private lateinit var memberArrayList: ArrayList<UserForRecycler>
    private lateinit var friendsInGroup: RecyclerView
    private lateinit var friendsArrayList: ArrayList<UserForRecycler>
    private lateinit var CURRENT_STATE : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_main_page)

        val viewGroupName : TextView = findViewById(R.id.GroupName)
        val viewGroupDesc : TextView = findViewById(R.id.GroupDesc)


    }

    private fun MaintainButton(){

    }

    private fun JoinGroup(){

    }

    private fun LeaveGroup(){

    }

}