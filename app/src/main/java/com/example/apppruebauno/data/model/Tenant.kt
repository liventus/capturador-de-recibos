package com.example.apppruebauno.data.model

data class Tenant(
    val id: String,
    val slug: String,
    val name: String,
    val plan: String,
    val status: String,
    val countryCode: String
)
