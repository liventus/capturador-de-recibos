package com.example.apppruebauno.ui.scanner

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R

class RecepcionMotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recepcion_moto)

        val scannedVin = intent.getStringExtra("SCAN_VIN") ?: "JHM CB 7652 MC 000001"
        findViewById<TextView>(R.id.etVin).text = scannedVin

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<View>(R.id.btnAnterior).setOnClickListener { finish() }

        findViewById<View>(R.id.btnGuardar).setOnClickListener {
            Toast.makeText(this, "Recepción guardada con éxito", Toast.LENGTH_LONG).show()
            
            // Crear intent para volver al HomeActivity
            val intent = android.content.Intent(this, com.example.apppruebauno.ui.home.HomeActivity::class.java)
            // Limpiar el stack de actividades para que no se pueda volver atrás a la edición
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }
}
