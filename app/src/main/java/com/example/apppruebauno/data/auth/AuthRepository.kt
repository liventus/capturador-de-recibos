package com.example.apppruebauno.data.auth

import com.example.apppruebauno.data.network.ApiService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth, private val apiService: ApiService) {
    // Su única responsabilidad: Autenticar y devolver el Token de Firebase
    suspend fun signIn(email: String, pass: String): String? {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        return result.user?.getIdToken(true)?.await()?.token
    }
    // Su única responsabilidad: Llamar a la API para traer tiendas
    suspend fun getTenants(token: String) = apiService.login("Bearer $token")

    /// Paso 1 de la cadena: Datos del usuario en la tienda seleccionada
    suspend fun getMe(slug: String, token: String) =
        apiService.loginSlug(slug, "Bearer $token")

    // Paso 2 de la cadena: Configuración de la tienda
    suspend fun getConfig(slug: String, token: String) =
        apiService.loginConfigTenant(slug, "Bearer $token")

}