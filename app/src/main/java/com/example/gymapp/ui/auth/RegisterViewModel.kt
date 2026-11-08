package com.example.gymapp.ui.auth



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val nombre: String = "",
    val correo: String = "",
    val password: String = "",
    val confirmacion: String = "",
    val nombreError: String? = null,
    val correoError: String? = null,
    val passwordError: String? = null,
    val confirmacionError: String? = null,
    val errorGeneral: String? = null,
    val isLoading: Boolean = false,
    val registroExitoso: Boolean = false
)

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNombreChange(value: String) =
        _uiState.update { it.copy(nombre = value, nombreError = null, errorGeneral = null) }

    fun onCorreoChange(value: String) =
        _uiState.update { it.copy(correo = value, correoError = null, errorGeneral = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null, errorGeneral = null) }

    fun onConfirmacionChange(value: String) =
        _uiState.update { it.copy(confirmacion = value, confirmacionError = null, errorGeneral = null) }

    fun onRegisterClick() {
        val s = _uiState.value
        if (s.isLoading) return

        val nombreError = AuthValidators.nombre(s.nombre)
        val correoError = AuthValidators.correo(s.correo)
        val passwordError = AuthValidators.password(s.password)
        val confirmacionError = AuthValidators.confirmacion(s.password, s.confirmacion)

        if (listOf(nombreError, correoError, passwordError, confirmacionError).any { it != null }) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    correoError = correoError,
                    passwordError = passwordError,
                    confirmacionError = confirmacionError
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }
        viewModelScope.launch {
            authRepository.register(s.nombre.trim(), s.correo.trim(), s.password)
                .onSuccess { _ ->
                    _uiState.update { it.copy(isLoading = false, registroExitoso = true) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorGeneral = e.message ?: "No se pudo crear la cuenta")
                    }
                }
        }
    }
}