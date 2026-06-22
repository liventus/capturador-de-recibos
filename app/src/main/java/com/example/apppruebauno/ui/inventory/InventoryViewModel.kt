package com.example.apppruebauno.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apppruebauno.data.auth.AuthRepository
import com.example.apppruebauno.data.model.InventoryItem
import com.example.apppruebauno.data.model.InventoryStatus
import com.example.apppruebauno.data.repository.IInventoryRepository
import com.example.apppruebauno.ui.login.LoginViewModel

// ui/inventory/InventoryViewModel.kt
class InventoryViewModel(private val repository: IInventoryRepository) :
    ViewModel() {

    private val _items = MutableLiveData<List<InventoryItem>>()
    val items: LiveData<List<InventoryItem>> get() = _items

    fun loadInventory() {
        _items.value = repository.getInventoryItems()
    }

    // Lógica para el resumen (28 OK, 2 Faltantes)
    fun getSummary(): Pair<Int, Int> {
        val list = _items.value ?: emptyList()
        val ok = list.count { it.estado == InventoryStatus.OK }
        val faltantes = list.count { it.estado == InventoryStatus.FALTANTE }
        return Pair(ok, faltantes)
    }
}

class InventoryViewModelFactory(private val repository: IInventoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InventoryViewModel(repository) as T
    }
}
