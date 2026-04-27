package com.example.futbolapp.model

data class Entrenador(
    val idEntrenador: Long? = null,
    val nombre: String,
    val especialidad: String,
    val equipo: Equipo? = null, // Objeto completo para recibir datos
    val idEquipo: Long? = null   // ID para enviar datos
)
