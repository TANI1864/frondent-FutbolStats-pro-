package com.example.futbolapp.model

data class JugadorRequest(
    val nombre: String,
    val posicion: String,
    val dorsal: Int,
    val fechaNac: String = "2000-01-01",
    val nacionalidad: String = "Colombiano",
    val equipo: EquipoRef
)
