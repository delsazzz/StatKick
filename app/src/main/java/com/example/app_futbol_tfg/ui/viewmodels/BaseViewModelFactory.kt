package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BaseViewModelFactory<T : ViewModel>(
    private val viewModelClass: Class<T>,
    private val creator: () -> T
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return creator() as VM
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}