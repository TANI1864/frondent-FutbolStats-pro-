package com.example.futbolapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futbolapp.data.repository.PartidoRepository
import com.example.futbolapp.model.Partido
import com.example.futbolapp.model.PartidoRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PartidoViewModel : ViewModel() {

    private val repo = PartidoRepository()

    private val _partidos = MutableStateFlow<List<Partido>>(emptyList())
    val partidos: StateFlow<List<Partido>> = _partidos

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getPartidos() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _partidos.value = repo.getPartidos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar partidos: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun createPartido(partido: PartidoRequest) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.createPartido(partido)
                getPartidos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _cargando.value = false
            }
        }
    }

    fun deletePartido(id: Long) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.deletePartido(id)
                getPartidos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al eliminar partido: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
