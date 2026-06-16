package com.example.apppruebauno.data.network

import com.example.apppruebauno.data.model.ConfigResponse
import com.example.apppruebauno.data.model.MeResponse
import com.example.apppruebauno.data.model.TenantResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("/api/v1/me/tenants")
    suspend fun login(
        @Header("Authorization") authHeader: String
    ): Response<List<TenantResponse>>

    @GET("/api/v1/me")
    suspend fun loginSlug(
        @Header("X-Tenant-Slug") XTenantSlug: String,
        @Header("Authorization") authHeader: String
    ): Response<MeResponse>

    @GET("/api/v1/config")
    suspend fun loginConfigTenant(
        @Header("X-Tenant-Slug") XTenantSlug: String,
        @Header("Authorization") authHeader: String
    ): Response<ConfigResponse>
}
