package com.example.apppruebauno.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R

class ModuloAdapter(private val modulos: List<String>) :
    RecyclerView.Adapter<ModuloAdapter.ModuloViewHolder>() {

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

        // Mapeo de nombre de módulo -> Icono y Nombre legible
        val (icono, nombreLimpio) = when (modulo) {
            "evaluaciones" -> "📝" to "Evaluaciones"
            "contratos" -> "📄" to "Contratos"
            "cobranzas" -> "💰" to "Cobranzas"
            "validacion" -> "✅" to "Validación"
            "finanzas" -> "🏦" to "Finanzas"
            "calculadora" -> "🧮" to "Calculadora"
            "calculadora.config" -> "⚙️" to "Config. Calc"
            "gestion-usuarios" -> "👥" to "Usuarios"
            "audit-log" -> "📋" to "Auditoría"
            else -> "📦" to modulo.replace("-", " ").capitalize()
        }

        holder.tvIcono.text = icono
        holder.tvNombre.text = nombreLimpio
    }

    override fun getItemCount() = modulos.size
}