package com.example.apppruebauno.data.model

data class WalletItem(
    val id: String,
    val name: String,
    val model: String,
    val status: String,
    val time: String,
    val initial: String,
    val statusColor: String // Hex color for the status text/background
)
