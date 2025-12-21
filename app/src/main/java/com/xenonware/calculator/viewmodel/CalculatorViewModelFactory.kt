package com.xenonware.calculator.viewmodel

// Create a new file: CalculatorViewModelFactory.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xenonware.calculator.data.SharedPreferenceManager

class CalculatorViewModelFactory(
    private val sharedPreferenceManager: SharedPreferenceManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalculatorViewModel(sharedPreferenceManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}