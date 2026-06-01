package com.example.apppruebauno.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R
import com.example.apppruebauno.ui.factura.FacturaActivity
import com.example.apppruebauno.ui.lista.ListaActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val cardRegistrarCompra = findViewById<LinearLayout>(R.id.cardRegistrarCompra)
        val cardVerFacturas = findViewById<LinearLayout>(R.id.cardVerFacturas)
        
        cardRegistrarCompra.setOnClickListener {
            val intent = Intent(this, FacturaActivity::class.java)
            startActivity(intent)
        }

        cardVerFacturas.setOnClickListener {
            val intent = Intent(this, ListaActivity::class.java)
            startActivity(intent)
        }
    }
}
