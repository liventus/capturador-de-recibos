package com.example.apppruebauno.ui.wallet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R
import com.example.apppruebauno.data.model.WalletItem

class WalletAdapter(
    private val items: List<WalletItem>,
    private val onItemClick: (WalletItem) -> Unit
) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    class WalletViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvInitial: TextView = view.findViewById(R.id.tvInitial)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvModel: TextView = view.findViewById(R.id.tvModel)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallet, parent, false)
        return WalletViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val item = items[position]
        holder.tvInitial.text = item.initial
        holder.tvName.text = item.name
        holder.tvModel.text = item.model
        holder.tvStatus.text = item.status
        holder.tvTime.text = item.time
        
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        
        try {
            holder.tvStatus.setTextColor(Color.parseColor(item.statusColor))
        } catch (e: Exception) {}
    }

    override fun getItemCount() = items.size
}
