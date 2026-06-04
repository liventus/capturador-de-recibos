package com.example.apppruebauno.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apppruebauno.R
import com.example.apppruebauno.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

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

                                    // Aquí tienes el JWT completo
                                    Log.d("OAUTH2", "Tu JWT es: $idToken")

                                    // Buscar el TenantID en los claims
                                    val tenantId = tokenTask.result.claims["tenant_id"] ?: "Sin Tenant"
                                    Log.d("OAUTH2", "TenantID: $tenantId")

                                    Toast.makeText(this, "Login Exitoso", Toast.LENGTH_SHORT).show()
                                    
                                    irAlHome()
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

    private fun irAlHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
