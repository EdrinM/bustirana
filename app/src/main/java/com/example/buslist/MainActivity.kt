package com.example.buslist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.buslist.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var busListView: ListView
    private lateinit var dashboardContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // ListView integration
        busListView = findViewById(R.id.busListView)
        dashboardContainer = findViewById(R.id.fragment_dashboard)

        val busLines = arrayOf("Kombinat - Kinostudio", "Tirana e Re", "Unaza", "Selite", "Porcelan-Qender", "Sauk - Qender",
            "Uzina Traktori - Qender", "Instituti Bujqesor - Qender", "St Trenit - Qender - TEG", "Tufine - Kombinat",
            "Sharre - Uzina Dinamo")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, busLines)
        busListView.adapter = adapter

        busListView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val selectedBusLine = busLines[position]

            val intent = Intent(this@MainActivity, StationsActivity::class.java)
            intent.putExtra("busLine", selectedBusLine)
            startActivity(intent)
        }


        // Set a listener for navigation changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Show the ListView only when the Dashboard destination is selected
            val showListView = destination.id == R.id.navigation_dashboard
            dashboardContainer.visibility = if (showListView) View.VISIBLE else View.GONE
        }
    }
}
