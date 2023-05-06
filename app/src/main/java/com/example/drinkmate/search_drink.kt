package com.example.drinkmate

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity

class search_drink : AppCompatActivity() {
    // creating variables to help with searching
    lateinit var drinksLV: ListView
    lateinit var listAdapter: ArrayAdapter<String>
    lateinit var drinksList: ArrayList<String>;
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_drink)

        // initializing variables of list view with their ids.
        drinksLV = findViewById(R.id.LVdrinks)
        searchView = findViewById(R.id.searchViewid)

        // initializing list and adding data to list
        drinksList = ArrayList()
        drinksList.add("beer")
        drinksList.add("whiskey")
        drinksList.add("rum")
        drinksList.add("gin")
        drinksList.add("tonic")
        drinksList.add("vodka")

        // initializing list adapter and setting layout
        // for each list view item and adding array list to it.
        listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            drinksList
        )
        drinksLV.adapter = listAdapter

        // listening for searches
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })
    }
}