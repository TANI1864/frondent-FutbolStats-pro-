package com.example.futbolapp.model

data class PartidoDetalle(
    val id: Long,
    val estadio: String,
    val nombreLocal: String,
    val nombreVisitante: String,
    val golesLocal: Int,
    val golesVisitante: Int,
    val fecha: String? = null,
    val alineacionLocal: List<Jugador>? = emptyList(),
    val alineacionVisitante: List<Jugador>? = emptyList()
)
