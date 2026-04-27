package com.example.futbolapp.model

data class PartidoRequest(
    val fecha: String,
    val estadio: String,
    val equipoLocal: EquipoRef,
    val equipoVisita: EquipoRef,
    val golesLocal: Int,
    val golesVisita: Int
)

data class EquipoRef(
    val idEquipo: Long
)
