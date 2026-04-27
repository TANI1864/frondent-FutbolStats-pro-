package com.example.futbolapp.data.repository

import com.example.futbolapp.model.Equipo
import com.example.futbolapp.data.remote.RetrofitInstance

class EquipoRepository {
    // AJUSTE CRÍTICO: Usamos RetrofitInstance (el de Render) igual que en Jugadores
    private val api = RetrofitInstance.api

    suspend fun getEquipos(): List<Equipo> = api.getEquipos()
    suspend fun createEquipo(equipo: Equipo) = api.createEquipo(equipo)
    suspend fun deleteEquipo(id: Long) = api.deleteEquipo(id)
}
