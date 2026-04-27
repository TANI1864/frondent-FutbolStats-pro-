package com.example.futbolapp.model

data class Partido(
    val idPartido: Long? = null,
    val fecha: String = "2024-05-20",
    val estadio: String,
    val equipoLocal: Equipo,
    val equipoVisita: Equipo,
    val golesLocal: Int = 0,
    val golesVisita: Int = 0
)
