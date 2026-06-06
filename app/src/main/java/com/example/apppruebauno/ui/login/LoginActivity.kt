package com.example.apppruebauno.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apppruebauno.R
import com.example.apppruebauno.data.network.RetrofitClient
import com.example.apppruebauno.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener {
            val email = etUsuario.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // 2. Iniciar sesión con Firebase
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            // 3. Obtener el ID Token (JWT)
                            user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                                if (tokenTask.isSuccessful) {
                                    val idToken: String? = tokenTask.result.token
                                    
                                    if (idToken != null) {
                                        // Llamar al backend con el token de Firebase
                                        llamarBackendLogin("Bearer $idToken")
                                    } else {
                                        Toast.makeText(this, "Error al obtener token", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Log.e("OAUTH2", "Error: ${task.exception?.message}")
                            Toast.makeText(this, "Error de login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun llamarBackendLogin(bearerToken: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.login(bearerToken)
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                        putExtra("USER_TYPE", loginResponse.userType)
                        putStringArrayListExtra("MODULOS", ArrayList(loginResponse.modulos ?: emptyList()))
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LOGIN", "Error backend: ${response.errorBody()?.string()}")
                    Toast.makeText(this@LoginActivity, "Error en backend: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LOGIN", "Error connecting to backend", e)
                Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
