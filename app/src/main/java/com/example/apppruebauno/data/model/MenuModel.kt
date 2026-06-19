package com.example.apppruebauno.data.model

data class MenuConfig(
    val app_name: String,
    val menu: List<MenuCategoria>
)

data class MenuCategoria(
    val categoria: String,
    val items: List<HomeItem>
)

data class HomeItem(
    val id: String,
    val titulo: String,
    val icono: String
)
