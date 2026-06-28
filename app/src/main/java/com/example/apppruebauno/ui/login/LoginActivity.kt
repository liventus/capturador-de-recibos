package com.example.apppruebauno.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.apppruebauno.R
import com.example.apppruebauno.data.auth.AuthRepository
import com.example.apppruebauno.data.model.TenantResponse
import com.example.apppruebauno.data.network.RetrofitClient
import com.example.apppruebauno.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var btnBiometric: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. INICIALIZACIÓN
        val auth = FirebaseAuth.getInstance()
        val apiService = RetrofitClient.instance
        val repository = AuthRepository(auth, apiService)
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        btnBiometric = findViewById(R.id.btnBiometric)

        // 2. LA ACCIÓN: Solo avisamos al ViewModel
        btnLogin.setOnClickListener {
            val email = etUsuario.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            viewModel.onLoginClicked(email, pass)
        }

        btnBiometric.setOnClickListener {
            setupBiometric()
        }

        // Verificar si mostramos el botón de huella
        checkBiometricAvailability()

        // 3. LA MAGIA: Observamos el estado
        setupObservers()
    }

    private fun checkBiometricAvailability() {
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(BIOMETRIC_STRONG)
        
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            // El dispositivo permite biometría.
            // Verificamos si hay datos guardados
            val savedToken = getSavedData("saved_token")
            val savedEmail = getSavedData("saved_email")
            if (savedToken != null && savedEmail != null) {
                btnBiometric.visibility = View.VISIBLE
            } else {
                btnBiometric.visibility = View.GONE
            }
        } else {
            btnBiometric.visibility = View.GONE
        }
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
                    mostrarDialogoTiendas(state.tiendas, state.token, state.email)
                }
                is LoginViewModel.LoginState.Success -> {
                    saveLoginData(state.token, state.email)
                    navigateToHome(state)
                }
                else -> {}
            }
        }
    }

    private fun saveLoginData(token: String, email: String) {
        try {
            val masterKey = MasterKey.Builder(this)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                this,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            sharedPreferences.edit()
                .putString("saved_token", token)
                .putString("saved_email", email)
                .apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSavedData(key: String): String? {
        return try {
            val masterKey = MasterKey.Builder(this)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                this,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            sharedPreferences.getString(key, null)
        } catch (e: Exception) {
            null
        }
    }

    private fun mostrarDialogoTiendas(tiendas: List<TenantResponse>, token: String, email: String) {
        val nombres = tiendas.map { it.tenantName }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Selecciona una Tienda")
            .setItems(nombres) { _, which ->
                val tiendaSeleccionada = tiendas[which]
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

    private fun setupBiometric() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val token = getSavedData("saved_token")
                    val email = getSavedData("saved_email")
                    if (token != null && email != null) {
                        Toast.makeText(this@LoginActivity, "Acceso concedido con huella", Toast.LENGTH_SHORT).show()
                        viewModel.onBiometricLogin(token, email)
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@LoginActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@LoginActivity, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso Biométrico")
            .setSubtitle("Usa tu huella para entrar")
            .setNegativeButtonText("Usar contraseña")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
