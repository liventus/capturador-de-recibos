package com.example.apppruebauno.data.model

data class MeResponse(
    val uid: String,
    val email: String,
    val name: String?,
    val tenant: Tenant
)
