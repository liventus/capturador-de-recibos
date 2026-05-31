package com.example.apppruebauno.data.model

data class FacturaSunat(
    val ruc: String,
    val razonSocial: String,
    val tipoDocumento: String,
    val serie: String,
    val numero: String,
    val fechaEmision: String,
    val total: Double,
    val estado: String
)