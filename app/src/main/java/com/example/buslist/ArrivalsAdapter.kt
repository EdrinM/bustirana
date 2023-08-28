package com.example.buslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArrivalsAdapter(private val arrivalsData: List<String>) :
    RecyclerView.Adapter<ArrivalsAdapter.ArrivalViewHolder>() {

    inner class ArrivalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val arrivalTextView: TextView = itemView.findViewById(R.id.arrivalTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrivalViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_arrival, parent, false)
        return ArrivalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArrivalViewHolder, position: Int) {
        val arrivalItem = arrivalsData[position]
        holder.arrivalTextView.text = arrivalItem
    }

    override fun getItemCount(): Int {
        return arrivalsData.size
    }
}
