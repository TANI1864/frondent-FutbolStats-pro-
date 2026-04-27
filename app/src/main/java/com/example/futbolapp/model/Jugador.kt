package com.example.futbolapp.model

data class Jugador(
    val idJugador: Long? = null,
    val nombre: String,
    val posicion: String,
    val dorsal: Int,
    val fechaNac: String = "2000-01-01",
    val nacionalidad: String = "Colombiano",
    val equipo: Equipo? = null, // Objeto completo para recibir datos
    val idEquipo: Long? = null   // ID para enviar datos
)
