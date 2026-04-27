package com.example.futbolapp.interfaces

import com.example.futbolapp.model.*
import retrofit2.http.*

interface ApiService {

    // =======================
    // 🔵 EQUIPOS
    // =======================
    @GET("equipos")
    suspend fun getEquipos(): List<Equipo>

    @POST("equipos")
    suspend fun createEquipo(@Body equipo: Equipo): Equipo

    @DELETE("equipos/{id}")
    suspend fun deleteEquipo(@Path("id") id: Long)


    // =======================
    // 🟢 JUGADORES
    // =======================
    @GET("jugadores")
    suspend fun getJugadores(): List<Jugador>

    @POST("jugadores")
    suspend fun createJugador(@Body request: JugadorRequest): Jugador

    @DELETE("jugadores/{id}")
    suspend fun deleteJugador(@Path("id") id: Long)

    @GET("jugadores/equipo/{id}")
    suspend fun getJugadoresPorEquipo(@Path("id") id: Long): List<Jugador>

    @GET("jugadores/goleadores/{goles}")
    suspend fun getGoleadores(@Path("goles") goles: Int): List<Jugador>


    // =======================
    // 🟡 ENTRENADORES
    // =======================
    @GET("entrenadores")
    suspend fun getEntrenadores(): List<Entrenador>

    @POST("entrenadores")
    suspend fun createEntrenador(@Body request: EntrenadorRequest): Entrenador

    @DELETE("entrenadores/{id}")
    suspend fun deleteEntrenador(@Path("id") id: Long)


    // =======================
    // 🔴 PARTIDOS
    // =======================
    @GET("partidos")
    suspend fun getPartidos(): List<Partido>

    @POST("partidos")
    suspend fun createPartido(@Body partido: PartidoRequest): Partido

    @DELETE("partidos/{id}")
    suspend fun deletePartido(@Path("id") id: Long)

    @GET("partidos/resultados")
    suspend fun getResultados(): List<ResultadoPartidoDTO>


    // =======================
    // 🟣 ESTADISTICAS
    // =======================
    @GET("estadisticas")
    suspend fun getEstadisticas(): List<EstadisticaJugador>

    @POST("estadisticas")
    suspend fun createEstadistica(@Body estadistica: EstadisticaRequest): EstadisticaJugador

    @DELETE("estadisticas/{id}")
    suspend fun deleteEstadistica(@Path("id") id: Long)

    @GET("estadisticas/goles-equipo/{id}")
    suspend fun getGolesEquipo(@Path("id") id: Long): Int
}
