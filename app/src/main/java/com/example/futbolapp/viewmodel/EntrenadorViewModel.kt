package com.example.futbolapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futbolapp.data.repository.EntrenadorRepository
import com.example.futbolapp.model.Entrenador
import com.example.futbolapp.model.EntrenadorRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EntrenadorViewModel : ViewModel() {

    private val repo = EntrenadorRepository()

    private val _entrenadores = MutableStateFlow<List<Entrenador>>(emptyList())
    val entrenadores: StateFlow<List<Entrenador>> = _entrenadores

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarEntrenadores() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _entrenadores.value = repo.getEntrenadores()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar entrenadores: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearEntrenador(request: EntrenadorRequest) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.crearEntrenador(request)
                cargarEntrenadores()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al registrar entrenador: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun borrarEntrenador(id: Long) {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.borrarEntrenador(id)
                cargarEntrenadores()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al eliminar entrenador: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
