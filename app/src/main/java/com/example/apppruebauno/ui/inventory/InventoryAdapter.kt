package com.example.apppruebauno.ui.inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R
import com.example.apppruebauno.data.model.InventoryItem
import com.example.apppruebauno.data.model.InventoryStatus

class InventoryAdapter(private var items: List<InventoryItem>) :
    RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    class InventoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStatusIcon: ImageView = view.findViewById(R.id.ivStatusIcon)
        val tvVin: TextView = view.findViewById(R.id.tvVin)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvStatusLabel: TextView = view.findViewById(R.id.tvStatusLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventory, parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = items[position]
        holder.tvVin.text = item.vin
        holder.tvCategoria.text = item.categoria
        holder.tvStatusLabel.text = item.estado.name

        when (item.estado) {
            InventoryStatus.OK -> {
                holder.ivStatusIcon.setImageResource(android.R.drawable.checkbox_on_background)
                holder.ivStatusIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark))
                holder.tvStatusLabel.setBackgroundResource(R.drawable.bg_chip_ok)
                holder.tvStatusLabel.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark))
            }
            InventoryStatus.FALTANTE -> {
                holder.ivStatusIcon.setImageResource(android.R.drawable.ic_delete)
                holder.ivStatusIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark))
                holder.tvStatusLabel.setBackgroundResource(R.drawable.bg_chip_error)
                holder.tvStatusLabel.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark))
            }
            InventoryStatus.EXTRA -> {
                holder.ivStatusIcon.setImageResource(android.R.drawable.ic_dialog_alert)
                holder.ivStatusIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_dark))
                holder.tvStatusLabel.setBackgroundResource(R.drawable.bg_chip_error) // Podrías crear uno naranja
                holder.tvStatusLabel.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_dark))
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<InventoryItem>) {
        items = newList
        notifyDataSetChanged()
    }
}
