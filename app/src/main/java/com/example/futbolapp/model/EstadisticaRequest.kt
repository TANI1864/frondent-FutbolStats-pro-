package com.example.futbolapp.model

data class EstadisticaRequest(
    val jugador: JugadorRef,
    val partido: PartidoRef,
    val minutosJugados: Int,
    val goles: Int,
    val asistencias: Int,
    val tarjetasAmarillas: Int,
    val tarjetasRojas: Int
)

data class JugadorRef(val idJugador: Long)
data class PartidoRef(val idPartido: Long)
