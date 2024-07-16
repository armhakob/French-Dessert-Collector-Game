package com.example.frenchdessertcollectorgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultAdapter(private val context: Context, private val items: List<Score>) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.rank.text = item.rank
        holder.name.text = item.name
        holder.time.text = item.time
        holder.score.text = item.totalScore
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val rank = view.findViewById<TextView>(R.id.rank)
        val name = view.findViewById<TextView>(R.id.name)
        val time = view.findViewById<TextView>(R.id.time)
        val score = view.findViewById<TextView>(R.id.score)
    }
}