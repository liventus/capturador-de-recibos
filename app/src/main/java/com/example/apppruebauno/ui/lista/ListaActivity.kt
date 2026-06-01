package com.example.apppruebauno.ui.lista

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R

class ListaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista)

        val btnBack = findViewById<ImageButton>(R.id.btnBackLista)
        btnBack.setOnClickListener {
            finish() // Regresa a la pantalla anterior (HomeActivity)
        }
    }
}
