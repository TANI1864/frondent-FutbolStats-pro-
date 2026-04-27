package com.example.futbolapp.data.repository
import com.example.futbolapp.data.remote.RetrofitInstance
import com.example.futbolapp.model.EstadisticaJugador
import com.example.futbolapp.model.EstadisticaRequest

class EstadisticaRepository {
    private val api = RetrofitInstance.api

    suspend fun getEstadisticas(): List<EstadisticaJugador> = api.getEstadisticas()

    suspend fun crearEstadistica(request: EstadisticaRequest) = api.createEstadistica(request)

    suspend fun deleteEstadistica(id: Long) = api.deleteEstadistica(id)

    suspend fun getGolesEquipo(id: Long): Int = api.getGolesEquipo(id)
}
