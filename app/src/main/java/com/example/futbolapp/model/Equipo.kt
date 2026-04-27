package com.example.futbolapp.model

data class Equipo(
    val idEquipo: Long? = null,
    val nombre: String,
    val ciudad: String,
    val fundacion: String = "2024-05-20"
)
