package com.example.gymapp.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.model.Rutina
import com.example.gymapp.domain.repository.AuthRepository
import com.example.gymapp.domain.repository.RutinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoutineListUiState(
    val isLoading: Boolean = false,
    val rutinas: List<Rutina> = emptyList(),
    val error: String? = null,
    val isLoggedOut: Boolean = false
)

class RoutineListViewModel(
    private val rutinaRepository: RutinaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutineListUiState())
    val uiState: StateFlow<RoutineListUiState> = _uiState.asStateFlow()

    init {
        loadRutinas()
    }

    fun loadRutinas() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            rutinaRepository.getRutinas()
                .onSuccess { rutinas ->
                    _uiState.update { it.copy(isLoading = false, rutinas = rutinas, error = null) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "No se pudieron cargar las rutinas"
                        )
                    }
                }
        }
    }

    fun logout() {
        authRepository.logout()
        _uiState.update { it.copy(isLoggedOut = true) }
    }
}
