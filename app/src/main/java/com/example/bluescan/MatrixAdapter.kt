package com.example.bluescan

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MatrixAdapter(
    private val context: Context,
    private var config: ProjectConfig,
    private var data: Map<Int, Map<String, String>>
) : RecyclerView.Adapter<MatrixAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUnitId: TextView = view.findViewById(R.id.tvUnitId)
        val llPartsContainer: LinearLayout = view.findViewById(R.id.llPartsContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_matrix_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unitNumber = config.startUnit + position
        holder.tvUnitId.text = "Unit $unitNumber"
        
        holder.llPartsContainer.removeAllViews()
        
        val unitData = data[unitNumber] ?: emptyMap()

        for (part in config.parts) {
            val cellView = LayoutInflater.from(context).inflate(R.layout.item_matrix_cell, holder.llPartsContainer, false) as TextView
            cellView.text = unitData[part] ?: ""
            // Color code for fun? ok if "Duplicate" or something
            holder.llPartsContainer.addView(cellView)
        }
    }

    override fun getItemCount(): Int {
        return (config.endUnit - config.startUnit) + 1
    }
    
    fun updateData(newConfig: ProjectConfig, newData: Map<Int, Map<String, String>>) {
        this.config = newConfig
        this.data = newData
        notifyDataSetChanged()
    }
}
