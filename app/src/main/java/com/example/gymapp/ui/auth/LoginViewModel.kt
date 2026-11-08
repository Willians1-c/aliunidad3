package com.example.gymapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val correoError: String? = null,
    val passwordError: String? = null,
    val errorGeneral: String? = null,
    val isLoading: Boolean = false,
    val loginExitoso: Boolean = false
)

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCorreoChange(value: String) =
        _uiState.update { it.copy(correo = value, correoError = null, errorGeneral = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null, errorGeneral = null) }

    fun onLoginClick() {
        val s = _uiState.value
        if (s.isLoading) return

        val correoError = AuthValidators.correo(s.correo)
        // En login solo exigimos que no esté vacía (la cuenta pudo crearse con otras reglas)
        val passwordError = if (s.password.isBlank()) "La contraseña es obligatoria" else null

        if (correoError != null || passwordError != null) {
            _uiState.update { it.copy(correoError = correoError, passwordError = passwordError) }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }
        viewModelScope.launch {
            authRepository.login(s.correo.trim(), s.password)
                .onSuccess { _ ->
                    _uiState.update { it.copy(isLoading = false, loginExitoso = true) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorGeneral = e.message ?: "No se pudo iniciar sesión")
                    }
                }
        }
    }
}