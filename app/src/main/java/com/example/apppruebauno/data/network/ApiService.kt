package com.example.apppruebauno.data.network

import com.example.apppruebauno.data.model.LoginResponse
import com.example.apppruebauno.data.model.TenantRequest
import com.example.apppruebauno.data.model.TenantResponse
import com.example.apppruebauno.data.model.Tenants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("/api/v1/platform/tenants")
    suspend fun login(
        @Header("Authorization") authHeader: String
    ): Response<List<TenantResponse>>

    @POST("/api/v1/platform/tenants")
    suspend fun tenants(
        @Header("Authorization") authHeader: String,
        @Body request: TenantRequest
    ): Response<TenantResponse>
}
