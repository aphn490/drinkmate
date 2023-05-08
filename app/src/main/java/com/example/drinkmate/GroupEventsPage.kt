package com.example.drinkmate

import android.content.Intent
import android.media.metrics.Event
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import org.w3c.dom.Document

class GroupEventsPage : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var eventRecyclerView : RecyclerView
    private lateinit var eventArrayList : ArrayList<EventForRecycler>
    private lateinit var myEventAdapter: EventAdapter
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_events_page)

        eventRecyclerView = findViewById(R.id.eventsRecycler)
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        eventRecyclerView.setHasFixedSize(true)

        eventArrayList = arrayListOf()
        myEventAdapter = EventAdapter(eventArrayList)

        val bundle : Bundle?= intent.extras
        val gn = bundle!!.getString("GroupName")


        eventRecyclerView.adapter = myEventAdapter
        myEventAdapter.setOnItemClickListener(object : EventAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@GroupEventsPage, EventPage::class.java)
                intent.putExtra("Name", eventArrayList[position].EventName)
                intent.putExtra("Desc", eventArrayList[position].Description)
                intent.putExtra("Date", eventArrayList[position].Date)
                intent.putExtra("Loc", eventArrayList[position].location)
                intent.putExtra("Time", eventArrayList[position].time)
                intent.putExtra("GroupName", gn)
                startActivity(intent)
            }
        })

        button = findViewById(R.id.AddEventButton)
        button.setOnClickListener {
            val intent = Intent(this, CreateEvent::class.java)
            intent.putExtra("GroupName", gn)
            startActivity(intent)
        }

        eventChangeListener()
    }

    private fun eventChangeListener() {
        val bundle : Bundle?= intent.extras
        val gn = bundle!!.getString("GroupName")
        db = FirebaseFirestore.getInstance()
        db.collection("Groups").document(gn!!).collection("events")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc : DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED){
                            eventArrayList.add(dc.document.toObject(EventForRecycler::class.java))
                        }
                    }
                    myEventAdapter.notifyDataSetChanged()
                }
            })
    }
}