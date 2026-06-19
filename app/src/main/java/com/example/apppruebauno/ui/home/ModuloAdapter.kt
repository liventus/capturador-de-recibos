package com.example.apppruebauno.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R

import com.example.apppruebauno.data.model.HomeItem

class ModuloAdapter(
    private val modulos: List<HomeItem>,
    private val onItemClick: (HomeItem) -> Unit
) : RecyclerView.Adapter<ModuloAdapter.ModuloViewHolder>() {

    class ModuloViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcono: TextView = view.findViewById(R.id.tvIconoModulo)
        val tvNombre: TextView = view.findViewById(R.id.tvNombreModulo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_modulo, parent, false)
        return ModuloViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModuloViewHolder, position: Int) {
        val modulo = modulos[position]
        
        holder.itemView.setOnClickListener { onItemClick(modulo) }

        // Mapeo de icono de JSON -> Emoji (puedes cambiarlo después por Drawables)
        val emoji = when (modulo.icono) {
            "ic_fingerprint" -> "👤"
            "ic_home" -> "🏠"
            "ic_qr_code_scanner" -> "🔍"
            "ic_document_scanner" -> "📄"
            "ic_delivery" -> "🚚"
            "ic_compare" -> "⚖️"
            "ic_error_outline" -> "⚠️"
            "ic_swap_horiz" -> "🔄"
            "ic_wallet" -> "👛"
            "ic_person_add" -> "➕"
            "ic_description" -> "📝"
            "ic_verified_user" -> "🛡️"
            "ic_payment" -> "💳"
            "ic_notifications" -> "🔔"
            else -> "📦"
        }

        holder.tvIcono.text = emoji
        holder.tvNombre.text = modulo.titulo
    }

    override fun getItemCount() = modulos.size
}
