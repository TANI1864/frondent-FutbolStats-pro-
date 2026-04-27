package com.example.futbolapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futbolapp.data.repository.EquipoRepository
import com.example.futbolapp.model.Equipo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EquipoViewModel : ViewModel() {

    private val repository = EquipoRepository()

    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos: StateFlow<List<Equipo>> = _equipos

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarEquipos() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _equipos.value = repository.getEquipos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearEquipo(equipo: Equipo) {
        viewModelScope.launch {
            try {
                repository.createEquipo(equipo)
                cargarEquipos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun borrarEquipo(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteEquipo(id)
                cargarEquipos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
