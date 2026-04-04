package com.example.dostavator.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ShiftViewModel : ViewModel() {
    var isOnShift by mutableStateOf(false)
        private set

    var todayEarnings by mutableStateOf(0)
        private set

    fun startShift() {
        isOnShift = true
    }

    fun stopShift() {
        isOnShift = false
    }
}