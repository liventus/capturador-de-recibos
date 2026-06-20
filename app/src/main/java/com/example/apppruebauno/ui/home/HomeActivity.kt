package com.example.apppruebauno.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.apppruebauno.R
import com.example.apppruebauno.data.model.HomeItem
import com.example.apppruebauno.data.model.MenuCategoria
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
        val roleTemporal = intent.getStringExtra("ROLE_TEMPORAL") ?: "N/A"
        val storeName = intent.getStringExtra("STORE_NAME") ?: "Sin Tienda"
        val email = intent.getStringExtra("EMAIL") ?: "email"

        // Actualizar mensaje de bienvenida
        findViewById<TextView>(R.id.tvWelcomeMessage).text = "Hola, $email"

        // --- LÓGICA DE FILTRADO Y CATEGORÍA ---
        val menuCompleto = cargarConfiguracionMenu()
        
        // Buscamos la categoría usando el ID técnico (ej: WAREHOUSE)
        val categoriaEncontrada = menuCompleto?.menu?.find { 
            it.id_categoria.equals(roleTemporal, ignoreCase = true) 
        }

        // Si lo encuentra, usamos el nombre amigable (ALMACENERO), si no, el original
        val nombreMostrable = categoriaEncontrada?.categoria ?: roleTemporal
        val itemsFiltrados = categoriaEncontrada?.items ?: emptyList()

        // Mostrar Tienda como título y Rol como subtítulo
        supportActionBar?.title = storeName
        supportActionBar?.subtitle = nombreMostrable

        // Cargar el Fragment por defecto
        if (savedInstanceState == null) {
            if (roleTemporal.equals("WAREHOUSE", ignoreCase = true)) {
                // Si es Almacenero, vamos directo al detalle que tiene el nuevo diseño
                val detailFragment = ModuloDetailFragment.newInstance(nombreMostrable)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .commit()
            } else {
                // Para otros roles, mostramos la lista de módulos
                val listFragment = ModuloListFragment.newInstance(itemsFiltrados) { modulo ->
                    showModuloDetail(modulo)
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, listFragment)
                    .commit()
            }
        }
    }

    private fun showModuloDetail(modulo: HomeItem) {
        val detailFragment = ModuloDetailFragment.newInstance(modulo.titulo)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out,
            )
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
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
