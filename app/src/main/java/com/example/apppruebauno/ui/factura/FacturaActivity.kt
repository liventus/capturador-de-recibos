package com.example.apppruebauno.ui.factura

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R

class FacturaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura)

        val btnGuardarFactura = findViewById<Button>(R.id.btnGuardarFactura)

        btnGuardarFactura.setOnClickListener {
            // Aquí iría la lógica para guardar los datos en una base de datos o API
            
            Toast.makeText(this, "Factura guardada correctamente", Toast.LENGTH_SHORT).show()

            // Regresamos a la pantalla anterior (HomeActivity)
            finish()
        }
    }
}
