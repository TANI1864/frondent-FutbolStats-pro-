package com.example.futbolapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futbolapp.data.repository.JugadorRepository
import com.example.futbolapp.model.Jugador
import com.example.futbolapp.model.JugadorRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JugadorViewModel : ViewModel() {

    private val repo = JugadorRepository()

    private val _jugadores = MutableStateFlow<List<Jugador>>(emptyList())
    val jugadores: StateFlow<List<Jugador>> = _jugadores

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    fun cargarJugadores() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _jugadores.value = repo.getJugadores()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearJugador(request: JugadorRequest) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.crearJugador(request)
                cargarJugadores()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _cargando.value = false
            }
        }
    }

    fun borrarJugador(id: Long) {
        viewModelScope.launch {
            try {
                repo.borrarJugador(id)
                cargarJugadores()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cargarPorEquipo(id: Long) {
        viewModelScope.launch {
            try {
                _jugadores.value = repo.getPorEquipo(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
