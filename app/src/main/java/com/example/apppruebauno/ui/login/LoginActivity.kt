package com.example.apppruebauno.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.apppruebauno.R
import com.example.apppruebauno.data.auth.AuthRepository
import com.example.apppruebauno.data.model.TenantResponse
import com.example.apppruebauno.data.network.RetrofitClient
import com.example.apppruebauno.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. INICIALIZACIÓN (Esto se puede mejorar luego con Hilt)
        val auth = FirebaseAuth.getInstance()
        val apiService = RetrofitClient.instance
        val repository = AuthRepository(auth, apiService)
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        // 2. LA ACCIÓN: Solo avisamos al ViewModel
        btnLogin.setOnClickListener {
            val email = etUsuario.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            viewModel.onLoginClicked(email, pass)
        }

        // 3. LA MAGIA: Observamos el estado (Aquí aplicamos SRP)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is LoginViewModel.LoginState.Loading -> {
                    // Mostrar un ProgressBar
                }
                is LoginViewModel.LoginState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                is LoginViewModel.LoginState.SelectTenant -> {
                    // Llamamos a la función que muestra el diálogo
                    mostrarDialogoTiendas(state.tiendas, state.token, state.email)
                }
                is LoginViewModel.LoginState.Success -> {
                    // Navegamos al Home
                    navigateToHome(state)
                }
                else -> {}
            }
        }
    }

    private fun mostrarDialogoTiendas(tiendas: List<TenantResponse>, token: String, email: String) {
        val nombres = tiendas.map { it.tenantName }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Selecciona una Tienda")
            .setItems(nombres) { _, which ->
                val tiendaSeleccionada = tiendas[which]
                // Avisamos al ViewModel de la elección
                viewModel.selectTenant(tiendaSeleccionada.tenantSlug, token, tiendaSeleccionada.role, email)
            }
            .setCancelable(false)
            .show()
    }

    private fun navigateToHome(data: LoginViewModel.LoginState.Success) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("ROLE_TEMPORAL", data.role)
            putExtra("STORE_NAME", data.storeName)
            putExtra("UID", data.uid)
            putExtra("EMAIL", data.email)
            putExtra("CONFIG_SLUG", data.configSlug)
        }
        startActivity(intent)
        finish()
    }
}
