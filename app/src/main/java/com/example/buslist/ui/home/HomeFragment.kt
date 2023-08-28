package com.example.buslist.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.buslist.OpenStreetMapActivity
import com.example.buslist.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // List of station names
    private val stationNames = arrayOf(
        "Kombinat - Kinostudio",
        "Tirana e Re",
        "Unaza",
        "Selite",
        "Porcelan-Qender",
        "Sauk - Qender",
        "Uzina Traktori - Qender",
        "Instituti Bujqesor - Qender",
        "St Trenit - Qender - TEG",
        "Tufine - Kombinat",
        "Sharre - Uzina Dinamo"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val openMapButton = binding.openMapButton
        val listView = binding.listView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, stationNames)
        listView.adapter = adapter

        openMapButton.setOnClickListener {
            openMapButton.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }

        listView.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            val selectedStationName = stationNames[position]
            val intent = Intent(activity, OpenStreetMapActivity::class.java)
            intent.putExtra("stationName", selectedStationName)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
