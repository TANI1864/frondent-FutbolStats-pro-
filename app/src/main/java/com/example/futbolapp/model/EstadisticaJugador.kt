package com.example.futbolapp.model

data class EstadisticaJugador(
    val id: Long? = null,
    val jugador: Jugador? = null,
    val partido: Partido? = null,
    val minutosJugados: Int,
    val goles: Int,
    val asistencias: Int,
    val tarjetasAmarillas: Int,
    val tarjetasRojas: Int,
    // IDs opcionales para el envío
    val idJugador: Long? = null,
    val idPartido: Long? = null
)
