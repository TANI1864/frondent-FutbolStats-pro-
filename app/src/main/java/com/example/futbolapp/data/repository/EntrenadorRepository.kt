package com.example.futbolapp.data.repository

import com.example.futbolapp.data.remote.RetrofitInstance
import com.example.futbolapp.model.Entrenador
import com.example.futbolapp.model.EntrenadorRequest

class EntrenadorRepository {

    private val api = RetrofitInstance.api

    suspend fun getEntrenadores(): List<Entrenador> = api.getEntrenadores()

    suspend fun crearEntrenador(request: EntrenadorRequest) = api.createEntrenador(request)

    suspend fun borrarEntrenador(id: Long) = api.deleteEntrenador(id)
}
