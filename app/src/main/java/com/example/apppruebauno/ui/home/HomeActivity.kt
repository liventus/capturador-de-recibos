package com.example.apppruebauno.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbarHome)
        setSupportActionBar(toolbar)

        // Obtener datos del intent
        val userType = intent.getStringExtra("USER_TYPE") ?: "Invitado"
        val roleTemporal = intent.getStringExtra("ROLE_TEMPORAL") ?: "N/A"
        val storeName = intent.getStringExtra("STORE_NAME") ?: "Sin Tienda"
        val uid = intent.getStringExtra("UID") ?: "Desconocido"
        val configSlug = intent.getStringExtra("CONFIG_SLUG") ?: "N/A"
        val modulos = intent.getStringArrayListExtra("MODULOS") ?: arrayListOf()

        // Mostrar Tienda y Rol en el Toolbar (incluimos roleTemporal si lo deseas)
        supportActionBar?.title = "$storeName - $roleTemporal"

        // IMPRIMIR LOS TRES RESPONSES EN EL LOG (Datos extraídos del Intent)
        Log.d("HOME_DATA", "--- Datos de Sesión ---")
        Log.d("HOME_DATA", "Usuario (UID): $uid")
        Log.d("HOME_DATA", "Tienda (Slug): $configSlug")
        Log.d("HOME_DATA", "Tienda (Nombre): $storeName")
        Log.d("HOME_DATA", "Rol Temporal: $roleTemporal")
        Log.d("HOME_DATA", "Rol Firestore: $userType")
        Log.d("HOME_DATA", "Módulos Firestore: $modulos")
        Log.d("HOME_DATA", "-----------------------")

        val rvModulos = findViewById<RecyclerView>(R.id.rvModulos)
        rvModulos.layoutManager = GridLayoutManager(this, 2)
        rvModulos.adapter = ModuloAdapter(modulos)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                finish()
                true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
