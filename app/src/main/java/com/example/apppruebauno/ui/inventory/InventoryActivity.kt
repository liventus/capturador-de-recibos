package com.example.apppruebauno.ui.inventory

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R
import com.example.apppruebauno.data.repository.InventoryRepositoryImpl

class InventoryActivity : AppCompatActivity() {

    private lateinit var viewModel: InventoryViewModel
    private lateinit var adapter: InventoryAdapter
    
    private lateinit var tvStatusOk: TextView
    private lateinit var tvStatusFaltantes: TextView
    private lateinit var tvCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        tvStatusOk = findViewById(R.id.tvStatusOk)
        tvStatusFaltantes = findViewById(R.id.tvStatusFaltantes)
        tvCount = findViewById(R.id.tvCount)

        // Setup SOLID (Inyección manual)
        val repository = InventoryRepositoryImpl()
        val factory = InventoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[InventoryViewModel::class.java]

        setupRecyclerView()
        setupObservers()

        viewModel.loadInventory()
    }

    private fun setupRecyclerView() {
        val rvInventory = findViewById<RecyclerView>(R.id.rvInventory)
        rvInventory.layoutManager = LinearLayoutManager(this)
        adapter = InventoryAdapter(emptyList())
        rvInventory.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.items.observe(this) { lista ->
            adapter.updateList(lista)
            
            // Actualizar resumen
            val (ok, faltantes) = viewModel.getSummary()
            tvStatusOk.text = "$ok OK"
            tvStatusFaltantes.text = "$faltantes Faltantes"
            tvCount.text = "$ok/${lista.size}"
        }
    }
}
