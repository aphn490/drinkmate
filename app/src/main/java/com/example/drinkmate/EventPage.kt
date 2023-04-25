package com.example.drinkmate

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class EventPage : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var eventNameTextView: TextView
    private lateinit var eventDateTextView: TextView
    private lateinit var eventTimeTextView: TextView
    private lateinit var eventLocationTextView: TextView
    private lateinit var eventDescriptionTextView: TextView
    private lateinit var interactButton: Button
    private lateinit var attendeeRecyclerView: RecyclerView
    private lateinit var attendeeArrayList: ArrayList<UserForRecycler>
    private lateinit var myUserAdapter: UserAdapter
    private lateinit var STATUS : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_page)

        db = FirebaseFirestore.getInstance()

        eventNameTextView = findViewById(R.id.eventNamePage)
        eventDateTextView = findViewById(R.id.eventDatePage)
        eventTimeTextView = findViewById(R.id.eventTimePage)
        eventLocationTextView = findViewById(R.id.eventLocation)
        eventDescriptionTextView = findViewById(R.id.eventDescriptionPage)
        interactButton = findViewById(R.id.eventInteractButton)

        attendeeRecyclerView = findViewById(R.id.membersAttending)
        attendeeRecyclerView.layoutManager = LinearLayoutManager(this)
        attendeeRecyclerView.setHasFixedSize(true)
        attendeeArrayList = arrayListOf()
        myUserAdapter = UserAdapter(attendeeArrayList)
        attendeeRecyclerView.adapter = myUserAdapter


        val bundle : Bundle?= intent.extras
        val en = bundle!!.getString("Name")
        val edesc = bundle!!.getString("Desc")
        val eloc = bundle!!.getString("Loc")
        val etime = bundle!!.getString("Time")
        val edate = bundle!!.getString("Date")

        eventNameTextView.text = en
        eventDescriptionTextView.text = edesc
        eventTimeTextView.text = "TIME: ${etime}"
        eventLocationTextView.text = "LOCATION: ${eloc}"
        eventDateTextView.text = "DATE: ${edate}"

        STATUS = "no"

        interactButton.setOnClickListener{
            if (STATUS == "no"){
                addEvent()
            }
            if (STATUS == "yes"){
                leaveEvent()
            }
        }


        myUserAdapter.setOnItemClickListener(object : UserAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@EventPage,PersonAccount::class.java)
                intent.putExtra("UID", attendeeArrayList[position].UID)
                intent.putExtra("email", attendeeArrayList[position].email)
                startActivity(intent)
                //Toast.makeText(this@FindFriends, "CURRENT USER: " + auth.currentUser?.uid.toString(), Toast.LENGTH_SHORT).show()

            }

        })

        eventChangeListener()
        maintainButtons()


    }

    private fun leaveEvent() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val gn = bundle!!.getString("GroupName")
        val en = bundle!!.getString("Name")

        db.collection("Groups").document(gn!!).collection("events")
            .document(en!!).collection("attendees").document(userId).delete()
        db.collection("UserAccounts").document(userId).collection("events")
            .document(en).delete()

        STATUS = "no"
        interactButton.text = "Join Event"
        interactButton.setBackgroundColor(Color.GREEN)
    }

    private fun addEvent() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val gn = bundle!!.getString("GroupName")
        val en = bundle!!.getString("Name")
        val edesc = bundle!!.getString("Desc")
        val eloc = bundle!!.getString("Loc")
        val etime = bundle!!.getString("Time")
        val edate = bundle!!.getString("Date")

        val user = hashMapOf(
            "UID" to userId,
            "email" to auth.currentUser?.email.toString()
        )
        val event = hashMapOf(
            "GroupName" to gn,
            "Date" to edate,
            "Description" to edesc,
            "EventName" to en,
            "location" to eloc,
            "Time" to etime
        )

        db.collection("Groups").document(gn!!).collection("events")
            .document(en!!).collection("attendees").document(userId).set(user)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    db.collection("UserAccounts").document(userId).collection("events")
                        .document(en).set(event)
                        .addOnCompleteListener{ task2 ->
                            if (task2.isSuccessful) {
                                STATUS = "yes"
                                interactButton.text = "Leave Event"
                                interactButton.setBackgroundColor(Color.RED)
                            }
                        }
                }
            }

    }

    private fun maintainButtons() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.uid.toString()
        val bundle : Bundle?= intent.extras
        val en = bundle!!.getString("Name")
        db.collection("UserAccounts").document(userId).collection("events").document(en!!)
            .get().addOnSuccessListener { documentSnapShot ->
                val temp = documentSnapShot.data?.get("EventName")
                if (temp == en) {
                    STATUS = "yes"
                    interactButton.text = "Leave Event"
                    interactButton.setBackgroundColor(Color.RED)
                } else {
                    STATUS = "no"
                    interactButton.text = "Join Event"
                    interactButton.setBackgroundColor(Color.GREEN)
                }
            }
    }


    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        val bundle : Bundle?= intent.extras
        val gn = bundle!!.getString("GroupName")
        val en = bundle!!.getString("Name")
        db.collection("Groups").document(gn!!).collection("events").document(en!!).collection("attendees")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            attendeeArrayList.add(dc.document.toObject(UserForRecycler::class.java))
                        }
                    }

                    myUserAdapter.notifyDataSetChanged()

                }

            })

    }
}