package com.example.futbolapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futbolapp.data.repository.EstadisticaRepository
import com.example.futbolapp.model.EstadisticaJugador
import com.example.futbolapp.model.EstadisticaRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstadisticaViewModel : ViewModel() {

    private val repo = EstadisticaRepository()

    private val _estadisticas = MutableStateFlow<List<EstadisticaJugador>>(emptyList())
    val estadisticas: StateFlow<List<EstadisticaJugador>> = _estadisticas

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getEstadisticas() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _estadisticas.value = repo.getEstadisticas()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearEstadistica(request: EstadisticaRequest) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.crearEstadistica(request)
                getEstadisticas()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminarEstadistica(id: Long) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.deleteEstadistica(id)
                getEstadisticas()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "No se pudo eliminar la estadística"
            } finally {
                _cargando.value = false
            }
        }
    }

    private val _golesTotales = MutableStateFlow(0)
    val golesTotales: StateFlow<Int> = _golesTotales

    fun cargarGolesEquipo(id: Long) {
        viewModelScope.launch {
            try {
                _golesTotales.value = repo.getGolesEquipo(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
