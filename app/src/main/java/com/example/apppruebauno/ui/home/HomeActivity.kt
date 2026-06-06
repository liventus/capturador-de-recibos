package com.example.apppruebauno.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebauno.R
import com.example.apppruebauno.ui.factura.FacturaActivity
import com.example.apppruebauno.ui.lista.ListaActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configurar Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbarHome)
        setSupportActionBar(toolbar)

        // Obtener datos del intent (Backend Cloud Run)
        val userType = intent.getStringExtra("USER_TYPE") ?: "Invitado"
        val modulos = intent.getStringArrayListExtra("MODULOS") ?: arrayListOf()

        val rvModulos = findViewById<RecyclerView>(R.id.rvModulos)

        // Mostrar UserType en el Toolbar
        supportActionBar?.title = userType

        // Mostrar modulos en el Log
        Log.d("HOME", "Módulos disponibles: $modulos")

        rvModulos.layoutManager = GridLayoutManager(this, 2)
        rvModulos.adapter = ModuloAdapter(modulos)

        Log.d("HOME", "Módulos cargados en la interfaz: $modulos")
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
