package com.example.apppruebauno.data.model

data class TenantRequest(
    val slug: String,
    val name: String,
    val plan: String,
    val countryCode: String
)
