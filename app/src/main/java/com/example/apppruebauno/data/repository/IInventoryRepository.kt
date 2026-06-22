package com.example.apppruebauno.data.repository

import com.example.apppruebauno.data.model.InventoryItem
import com.example.apppruebauno.data.model.InventoryStatus

// data/repository/IInventoryRepository.kt
interface IInventoryRepository {
    fun getInventoryItems(): List<InventoryItem>
}

class InventoryRepositoryImpl : IInventoryRepository {
    override fun getInventoryItems(): List<InventoryItem> {
        // Mockeo de datos para tu prueba
        return listOf(
            InventoryItem("1", "JHMCB7652MC000001", "Moto Nueva", InventoryStatus.OK),
            InventoryItem("2", "JHMCB7652MC000002", "Moto Segunda", InventoryStatus.OK),
            InventoryItem("3", "JHMCB7652MC000003", "Repuesto", InventoryStatus.FALTANTE),
            InventoryItem("4", "JHMCB7652MC000004", "Moto Nueva", InventoryStatus.OK),
            InventoryItem("5", "JHMCB7652MC000005", "Repuesto", InventoryStatus.EXTRA)
        )
    }
}