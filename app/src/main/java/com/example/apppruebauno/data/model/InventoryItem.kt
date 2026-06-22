package com.example.apppruebauno.data.model


data class InventoryItem(
    val id: String,
    val vin: String, // El código largo que se ve en la imagen
    val categoria: String, // "Repuesto", "Moto Nueva", "Moto Segunda"
    val estado: InventoryStatus
)

enum class InventoryStatus {
    OK, FALTANTE, EXTRA
}