package com.example.apppruebauno.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R
import com.example.apppruebauno.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            // Aquí podrías agregar validaciones de usuario/password
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Cerramos el login para que no pueda volver atrás
        }
    }
}
