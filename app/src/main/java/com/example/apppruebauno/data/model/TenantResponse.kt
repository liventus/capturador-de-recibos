package com.example.apppruebauno.data.model

data class TenantResponse(
    val id: String,
    val slug: String,
    val name: String,
    val plan: String,
    val status: String,
    val countryCode: String,
    val schemaName: String,
    val trialEndsAt: String,
    val activatedAt: String?,
    val createdAt: String,
    val updatedAt: String
)
