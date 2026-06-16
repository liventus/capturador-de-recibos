package com.example.apppruebauno.data.model

data class ConfigResponse(
    val tenantId: String,
    val slug: String,
    val name: String,
    val countryCode: String,
    val plan: String,
    val theme: ThemeConfig,
    val modules: ModulesConfig
)

data class ThemeConfig(
    val primaryColor: String,
    val backgroundColor: String,
    val textColor: String,
    val fontFamily: String,
    val borderRadius: String,
    val brandName: String,
    val logoUrl: String?
)

data class ModulesConfig(
    val workshop: Boolean,
    val tradeIn: Boolean,
    val financing: Boolean
)
