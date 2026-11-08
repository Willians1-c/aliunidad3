package com.example.gymapp.ui

import androidx.lifecycle.ViewModel
import com.example.gymapp.domain.repository.AuthRepository
import com.example.gymapp.ui.navigation.Routes

class MainViewModel(authRepository: AuthRepository) : ViewModel() {
    val startDestination: String =
        if (authRepository.hasSession()) Routes.HOME else Routes.LOGIN
}