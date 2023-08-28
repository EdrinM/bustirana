package com.example.buslist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class StationsActivity : AppCompatActivity() {

    private lateinit var stationsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stations)

        stationsListView = findViewById(R.id.stationsListView)

        // Retrieve the selected bus line from the intent extras
        val selectedBusLine = intent.getStringExtra("busLine")

        // Use a map or a switch statement to determine the stations based on the selected bus line
        val stations = when (selectedBusLine) {
            "Kombinat - Kinostudio" -> arrayOf("Station A", "Station B", "Station C")
            "Tirana e Re" -> arrayOf("Station X", "Station Y", "Station Z")
            "Unaza" -> arrayOf("test")
            "Selite" -> arrayOf("test")
            "Porcelan-Qender" -> arrayOf("test")
            "Sauk - Qender" -> arrayOf("test")
            "Uzina Traktori - Qender" -> arrayOf("test")
            "Instituti Bujqesor - Qender" -> arrayOf("test")
            "St Trenit - Qender - TEG" -> arrayOf("test")
            "Tufine - Kombinat" -> arrayOf("test")
            "Sharre - Uzina Dinamo" -> arrayOf("test")
            // Add more cases for other bus lines
            else -> emptyArray() // Default case, or provide an empty array if no match
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stations)
        stationsListView.adapter = adapter

        stationsListView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            // Handle station item click, navigate to arrivals timetable page
            val intent = Intent(this@StationsActivity, ArrivalsTimetableActivity::class.java)
            startActivity(intent)
        }
    }
}


