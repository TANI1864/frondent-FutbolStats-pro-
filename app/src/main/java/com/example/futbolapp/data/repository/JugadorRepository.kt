package com.example.futbolapp.data.repository

import com.example.futbolapp.interfaces.ApiService
import com.example.futbolapp.data.remote.RetrofitInstance
import com.example.futbolapp.model.Jugador
import com.example.futbolapp.model.JugadorRequest

class JugadorRepository {

    private val api = RetrofitInstance.api

    suspend fun getJugadores(): List<Jugador> = api.getJugadores()

    suspend fun crearJugador(request: JugadorRequest) = api.createJugador(request)

    suspend fun borrarJugador(id: Long) = api.deleteJugador(id)

    suspend fun getPorEquipo(id: Long): List<Jugador> = api.getJugadoresPorEquipo(id)

    suspend fun getGoleadores(goles: Int): List<Jugador> = api.getGoleadores(goles)
}
