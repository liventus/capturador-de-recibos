package com.example.apppruebauno.ui.login

import androidx.lifecycle.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.viewModelScope
import com.example.apppruebauno.data.model.TenantResponse
import com.example.apppruebauno.data.auth.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    // 1. Definimos los ESTADOS de la pantalla
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Error(val message: String) : LoginState()
        // Agregamos todos los campos necesarios para el éxito total
        data class Success(
            val email: String,
            val role: String?,
            val storeName: String?,
            val uid: String?,
            val configSlug: String?
        ) : LoginState()
        data class SelectTenant(val tiendas: List<TenantResponse>, val token: String, val email: String) : LoginState()
    }

    private val _state = MutableLiveData<LoginState>(LoginState.Idle)
    val state: LiveData<LoginState> get() = _state

    fun onLoginClicked(email: String, pass: String) {
        if (email.isEmpty() || pass.isEmpty()) {
            _state.value = LoginState.Error("Campos obligatorios")
            return
        }

        _state.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val token = repository.signIn(email, pass)
                if (token != null) {
                    val response = repository.getTenants(token)
                    if (response.isSuccessful && response.body() != null) {
                        val tiendas = response.body()!!

                        if (tiendas.size == 1) {
                            // Si solo hay una, ejecutamos la cadena automáticamente
                            val tiendaUnica = tiendas[0]
                            selectTenant(tiendaUnica.tenantSlug, token, tiendaUnica.role, email)
                        } else {
                            // Si hay varias, pedimos selección
                            _state.value = LoginState.SelectTenant(tiendas, token, email)
                        }
                    } else {
                        _state.value = LoginState.Error("No se pudieron obtener las tiendas")
                    }
                } else {
                    _state.value = LoginState.Error("Error de autenticación")
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun selectTenant(slug: String, token: String, role: String, email: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val meResponse = repository.getMe(slug, token)
                val configResponse = repository.getConfig(slug, token)

                if (meResponse.isSuccessful && configResponse.isSuccessful) {
                    _state.value = LoginState.Success(
                        email = email,
                        role = role,
                        storeName = meResponse.body()?.tenant?.name ?: slug,
                        uid = meResponse.body()?.uid,
                        configSlug = configResponse.body()?.slug
                    )
                } else {
                    _state.value = LoginState.Error("Error al configurar la tienda")
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error("Fallo de conexión")
            }
        }
    }
}

class LoginViewModelFactory(private val repository: AuthRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}
