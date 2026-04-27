package com.example.futbolapp.model

data class ResultadoPartidoDTO(
    val equipoLocal: String,
    val equipoVisita: String,
    val golesLocal: Int,
    val golesVisita: Int
)