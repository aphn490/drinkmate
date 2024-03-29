package com.example.drinkmate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint.Join
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class GroupMainPage : AppCompatActivity() {

    private lateinit var interactButton: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var allGroupMembers: RecyclerView
    private lateinit var memberArrayList: ArrayList<UserForRecycler>
    private lateinit var friendsInGroup: RecyclerView
    private lateinit var friendsArrayList: ArrayList<UserForRecycler>
    private lateinit var CURRENT_STATE : String
    private lateinit var myUserAdapter1: UserAdapter
    private lateinit var myUserAdapter2: UserAdapter
    private lateinit var viewEventButton: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_main_page)
        // Display group info on screen
        val viewGroupName : TextView = findViewById(R.id.GroupName)
        val viewGroupDesc : TextView = findViewById(R.id.GroupDesc)
        val imageView : ImageView = findViewById(R.id.GroupIcon)
        interactButton = findViewById(R.id.InteractButton)
        viewEventButton = findViewById(R.id.ViewEvents)
        val bundle : Bundle?= intent.extras
        viewGroupName.text = bundle!!.getString("Name")
        viewGroupDesc.text = bundle!!.getString("Description")
        val src = bundle!!.getString("imgsrc")
        // Initialize both recycler views and set on click listeners
        allGroupMembers = findViewById(R.id.AllGroupMembers)
        friendsInGroup = findViewById(R.id.MutualFriends)
        allGroupMembers.layoutManager = LinearLayoutManager(this)
        friendsInGroup.layoutManager = LinearLayoutManager(this)
        allGroupMembers.setHasFixedSize(true)
        friendsInGroup.setHasFixedSize(true)
        memberArrayList = arrayListOf()
        friendsArrayList = arrayListOf()
        myUserAdapter1 = UserAdapter(memberArrayList)
        myUserAdapter2 = UserAdapter(friendsArrayList)
        allGroupMembers.adapter = myUserAdapter1
        friendsInGroup.adapter = myUserAdapter2

        CURRENT_STATE = "not_a_member"

//        if (src != ""){
//            val MAX_BYTES : Long = 10_000 * 10_000
//            val image = Firebase.storage.getReferenceFromUrl(src.toString()).getBytes(MAX_BYTES).result
//            val bm : Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
//            imageView.setImageBitmap(bm)
//
//        }

        myUserAdapter1.setOnItemClickListener(object : UserAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@GroupMainPage,PersonAccount::class.java)
                intent.putExtra("UID", memberArrayList[position].UID)
                intent.putExtra("email", memberArrayList[position].email)
                startActivity(intent)
                //Toast.makeText(this@FindFriends, "CURRENT USER: " + auth.currentUser?.uid.toString(), Toast.LENGTH_SHORT).show()

            }
        })

        myUserAdapter2.setOnItemClickListener(object: UserAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@GroupMainPage,PersonAccount::class.java)
                intent.putExtra("UID", friendsArrayList[position].UID)
                intent.putExtra("email", friendsArrayList[position].email)
                startActivity(intent)
                //Toast.makeText(this@FindFriends, "CURRENT USER: " + auth.currentUser?.uid.toString(), Toast.LENGTH_SHORT).show()

            }
        })

        eventChangeListener()

        interactButton.setOnClickListener{
            if (CURRENT_STATE == "not_a_member"){
                JoinGroup()
            }
            if (CURRENT_STATE == "member"){
                LeaveGroup()
            }
        }

        viewEventButton.setOnClickListener{
            val intent = Intent(this@GroupMainPage,GroupEventsPage::class.java)
            intent.putExtra("GroupName", viewGroupName.text)
            startActivity(intent)
        }

        MaintainButton()


    }

    /**
     * Ensure correct button is displayed on screen
     */
    private fun MaintainButton(){
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val groupName = bundle!!.getString("Name")
        val docRef = db.collection("UserAccounts").document(userId).collection("groups").document(groupName!!)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val temp = documentSnapshot.data?.get("GroupName")
            if (temp == groupName){
                CURRENT_STATE = "member"
                interactButton.text = "Leave Group"
                interactButton.setBackgroundColor(Color.RED)
                viewEventButton.visibility = View.VISIBLE
                viewEventButton.isEnabled = true
            } else {
                CURRENT_STATE = "not_a_member"
                interactButton.text = "Join"
                interactButton.setBackgroundColor(Color.GREEN)
                viewEventButton.visibility = View.INVISIBLE
                viewEventButton.isEnabled = false
            }
        }


    }

    /**
     * Click Button to Join Group
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun JoinGroup(){
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val groupName = bundle!!.getString("Name")
        // search for current UID in groups member list
        val groupRef = db.collection("Groups").document(groupName!!)
            .collection("members")

        val user = hashMapOf(
            "UID" to userId,
            "email" to auth.currentUser?.email.toString()
        )
        groupRef.document(userId).set(user).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                val group = hashMapOf(
                    "GroupName" to groupName,
                    "Member Since" to DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneOffset.UTC)
                        .format(Instant.now())
                )
                db.collection("UserAccounts").document(userId).collection("groups")
                    .document(groupName)
                    .set(group)
                    .addOnCompleteListener{ task2 ->
                        if(task2.isSuccessful){
                            CURRENT_STATE = "member"
                            interactButton.text = "Leave Group"
                            interactButton.setBackgroundColor(Color.RED)
                            viewEventButton.visibility = View.VISIBLE
                            viewEventButton.isEnabled = true
                        }
                    }
            }
        }

    }

    /**
     * Click Button to Leave Group
     */
    private fun LeaveGroup(){
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val groupName = bundle!!.getString("Name")
        db.collection("Groups").document(groupName!!).collection("members").document(userId).delete()
        db.collection("UserAccounts").document(userId).collection("groups").document(groupName).delete()
        CURRENT_STATE = "not_a_member"
        interactButton.text = "Join"
        interactButton.setBackgroundColor(Color.GREEN)
        viewEventButton.visibility = View.INVISIBLE
        viewEventButton.isEnabled = false
    }

    /**
     * Populate recycler views with correct user information
     */
    private fun eventChangeListener(){
        db = FirebaseFirestore.getInstance()
        val bundle : Bundle?= intent.extras
        val groupName = bundle!!.getString("Name")
        db.collection("Groups").document(groupName!!).collection("members")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            memberArrayList.add(dc.document.toObject(UserForRecycler::class.java))
                            friendsArrayList.add(dc.document.toObject(UserForRecycler::class.java))
                        }
                        if (dc.type == DocumentChange.Type.REMOVED){
                            myUserAdapter1.notifyDataSetChanged()
                            myUserAdapter2.notifyDataSetChanged()
                        }
                    }

                    myUserAdapter1.notifyDataSetChanged()
                    myUserAdapter2.notifyDataSetChanged()

                }

            })
    }

}