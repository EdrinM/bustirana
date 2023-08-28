package com.example.buslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArrivalsTimetableActivity : AppCompatActivity() {

    private lateinit var arrivalsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrivals_timetable)

        arrivalsRecyclerView = findViewById(R.id.arrivalsRecyclerView)

        val arrivalsData = mutableListOf("Arrival 1", "Arrival 2", "Arrival 3")

        val adapter = ArrivalsAdapter(arrivalsData)
        arrivalsRecyclerView.adapter = adapter
        arrivalsRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}

