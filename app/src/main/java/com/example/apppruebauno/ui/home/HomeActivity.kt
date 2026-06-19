package com.example.apppruebauno.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R

import com.example.apppruebauno.data.model.MenuConfig

import com.google.gson.Gson

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
        val email = intent.getStringExtra("EMAIL") ?: "email"

        // Actualizar mensaje de bienvenida con el email
        val tvWelcomeMessage = findViewById<TextView>(R.id.tvWelcomeMessage)
        tvWelcomeMessage.text = "$email"

        // Mostrar Tienda y Rol en el Toolbar
        supportActionBar?.title = "$storeName - $roleTemporal"

        // --- LÓGICA DE FILTRADO POR ROL USANDO EL JSON ---
        val menuCompleto = cargarConfiguracionMenu()
        

        val itemsFiltrados = menuCompleto?.menu
            ?.find { it.categoria.equals(roleTemporal, ignoreCase = true) }
            ?.items ?: emptyList()

        val rvModulos = findViewById<RecyclerView>(R.id.rvModulos)
        rvModulos.layoutManager = GridLayoutManager(this, 2)
        
        // Pasamos los items filtrados al adaptador
        rvModulos.adapter = ModuloAdapter(itemsFiltrados)
    }

    private fun cargarConfiguracionMenu(): MenuConfig? {
        return try {
            val jsonString = resources.openRawResource(R.raw.reglas)
                .bufferedReader()
                .use { it.readText() }
            Gson().fromJson(jsonString, MenuConfig::class.java)
        } catch (e: Exception) {
            Log.e("HOME_DATA", "Error al cargar reglas.json", e)
            null
        }
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}
