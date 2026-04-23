package com.example.bluescan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PartsAdapter(
    private val parts: MutableList<String>
) : RecyclerView.Adapter<PartsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPartName: TextView = view.findViewById(R.id.tvPartName)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeletePart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_part_editor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val part = parts[position]
        holder.tvPartName.text = part
        holder.btnDelete.setOnClickListener {
            parts.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, parts.size)
        }
    }

    override fun getItemCount() = parts.size

    fun addPart(name: String) {
        parts.add(name)
        notifyItemInserted(parts.size - 1)
    }
}
