package com.example.apppruebauno.data.model

data class LoginResponse(
    val token: String,
    val expiresIn: Long,
    val uid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val userType: String,
    val userCategory: String,
    val firstLogin: Boolean,
    val stores: List<StoreInfoDto>?,
    val modulos: List<String>?
)

data class StoreInfoDto(
    val id: String,
    val name: String
)
