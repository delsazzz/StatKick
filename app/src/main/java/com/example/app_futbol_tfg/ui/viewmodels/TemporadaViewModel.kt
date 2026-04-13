package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.TemporadaEntity
import com.example.app_futbol_tfg.data.repository.TemporadaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TemporadaViewModel(private val repository: TemporadaRepository) : ViewModel() {

    val temporadas: Flow<List<TemporadaEntity>> = repository.getAll()

    suspend fun getTemporadaById(id: Int): TemporadaEntity? {
        return try {
            repository.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getByTemporada(temporada: String): TemporadaEntity? {
        return try {
            repository.getByTemporada(temporada)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertTemporada(temporada: String) {
        viewModelScope.launch {
            try {
            repository.insertTemporada(
                TemporadaEntity(
                    temporada = temporada
                )
            )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}