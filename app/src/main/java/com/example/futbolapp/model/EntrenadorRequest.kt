package com.example.futbolapp.model

data class EntrenadorRequest(
    val nombre: String,
    val especialidad: String,
    val equipo: EquipoRef
)
