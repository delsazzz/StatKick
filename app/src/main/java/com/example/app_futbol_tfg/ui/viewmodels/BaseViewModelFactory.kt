package com.example.app_futbol_tfg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Factoría genérica donde creamos instancias de ViewModel personalizadas
// Esto permite inyectar dependencias manualmente sin necesidad de un framework externo
class BaseViewModelFactory<T : ViewModel>(
    private val viewModelClass: Class<T>,
    private val creator: () -> T
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        // Comprueba que la clase solicitada corresponde al ViewModel esperado
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return creator() as VM
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}