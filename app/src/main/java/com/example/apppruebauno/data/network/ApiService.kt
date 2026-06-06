package com.example.apppruebauno.data.network

import com.example.apppruebauno.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(
        @Header("Authorization") authHeader: String
    ): Response<LoginResponse>
}
