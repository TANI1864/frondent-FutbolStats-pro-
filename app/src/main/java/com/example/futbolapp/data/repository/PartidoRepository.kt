package com.example.futbolapp.data.repository

import com.example.futbolapp.model.Partido
import com.example.futbolapp.model.PartidoRequest
import com.example.futbolapp.model.ResultadoPartidoDTO
import com.example.futbolapp.data.remote.RetrofitInstance

class PartidoRepository {
    private val api = RetrofitInstance.api

    suspend fun getPartidos(): List<Partido> = api.getPartidos()
    
    suspend fun createPartido(partido: PartidoRequest) = api.createPartido(partido)
    
    suspend fun deletePartido(id: Long) = api.deletePartido(id)

    suspend fun getResultados(): List<ResultadoPartidoDTO> = api.getResultados()
}
