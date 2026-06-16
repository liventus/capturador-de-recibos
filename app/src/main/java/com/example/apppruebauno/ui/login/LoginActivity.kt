package com.example.apppruebauno.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apppruebauno.R
import com.example.apppruebauno.data.model.TenantResponse
import com.example.apppruebauno.data.network.RetrofitClient
import com.example.apppruebauno.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private  var roleTemporal: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        auth = FirebaseAuth.getInstance()

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener {
            val email = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                                if (tokenTask.isSuccessful) {
                                    val idToken = tokenTask.result.token
                                    if (idToken != null) {
                                        // Llamar al backend con el token de Firebase para traer tiendas
                                        llamarBackendTenants("Bearer $idToken", email)
                                    } else {
                                        Toast.makeText(this, "Error al obtener token", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            Log.e("LOGIN", "Error: ${task.exception?.message}")
                            Toast.makeText(this, "Error de login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun llamarBackendTenants(bearerToken: String, email: String) {
        lifecycleScope.launch {
            try {
                // Obtenemos la lista de tiendas (tenants) del backend externo
                val response = RetrofitClient.instance.login(bearerToken)
                
                if (response.isSuccessful && response.body() != null) {
                    val tiendas = response.body()!!
                    
                    when {
                        tiendas.size == 1 -> {
                            val tiendaUnica = tiendas[0]
                            Log.d("LOGIN", "Tienda única detectada: ${tiendaUnica.tenantName}")
                            roleTemporal = tiendaUnica.role
                            ejecutarChainLogin(bearerToken, tiendaUnica.tenantSlug, email)
                        }
                        tiendas.size > 1 -> {
                            mostrarDialogoTiendas(tiendas, bearerToken, email)
                        }
                        else -> {
                            Toast.makeText(this@LoginActivity, "No tienes tiendas asignadas", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("LOGIN", "Error backend: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Error al obtener tiendas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LOGIN", "Error de conexión", e)
                Toast.makeText(this@LoginActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoTiendas(tiendas: List<TenantResponse>, bearerToken: String, email: String) {
        val nombres = tiendas.map { it.tenantName}.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Selecciona una Tienda")
            .setItems(nombres) { _, which ->
                val slug = tiendas[which].tenantSlug
                roleTemporal = tiendas[which].role
                Log.d("LOGIN", "Tienda elegida Slug: $slug")
                ejecutarChainLogin(bearerToken, slug, email)
            }
            .setCancelable(false)
            .show()
    }

    private fun ejecutarChainLogin(bearerToken: String, slug: String, email: String) {
        lifecycleScope.launch {
            try {
                // 1. Consumir /api/v1/me con el Slug en el header
                val meResponse = RetrofitClient.instance.loginSlug(slug, bearerToken)
                
                // 2. Consumir /api/v1/config con el Slug en el header
                val configResponse = RetrofitClient.instance.loginConfigTenant(slug, bearerToken)
                
                if (meResponse.isSuccessful && configResponse.isSuccessful) {
                    val meData = meResponse.body()
                    val configData = configResponse.body()
                    
                    Log.d("LOGIN_CHAIN", "Me data: $meData")
                    Log.d("LOGIN_CHAIN", "Config data: $configData")
                    
                    // 3. Buscar el Rol y Módulos en Firestore
                    buscarPermisosFirestore(email, meData?.tenant?.name ?: slug, meData?.uid, configData?.slug)
                } else {
                    Toast.makeText(this@LoginActivity, "Error en la configuración de la tienda", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LOGIN_CHAIN", "Error en cadena: ${e.message}")
                Toast.makeText(this@LoginActivity, "Error al configurar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarPermisosFirestore(email: String, nombreTienda: String, uid: String?, configSlug: String?) {
        val db = FirebaseFirestore.getInstance()
        
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { userDocs ->
                if (!userDocs.isEmpty) {
                    val userType = userDocs.documents[0].getString("userType") ?: "GENERAL"
                    
                    db.collection("modulos")
                        .whereEqualTo("ROL", userType)
                        .get()
                        .addOnSuccessListener { moduloDocs ->
                            val listaModulos = if (!moduloDocs.isEmpty) {
                                moduloDocs.documents[0].get("MODULOS") as? List<String> ?: emptyList()
                            } else {
                                emptyList()
                            }

                            val intent = Intent(this, HomeActivity::class.java).apply {
                                putExtra("USER_TYPE", userType)
                                putExtra("ROLE_TEMPORAL", roleTemporal) // Enviamos el roleTemporal
                                putExtra("STORE_NAME", nombreTienda)
                                putExtra("UID", uid)
                                putExtra("EMAIL",email)
                                putExtra("CONFIG_SLUG", configSlug)
                                putStringArrayListExtra("MODULOS", ArrayList(listaModulos))
                            }
                            startActivity(intent)
                            finish()
                        }
                } else {
                    Toast.makeText(this, "Usuario no configurado en Firestore", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Error al cargar permisos", e)
            }
    }
}
