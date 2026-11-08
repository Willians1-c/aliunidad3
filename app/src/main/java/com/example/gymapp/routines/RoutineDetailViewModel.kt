package com.example.gymapp.routines

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.model.Rutina
import com.example.gymapp.domain.repository.RutinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoutineDetailUiState(
    val isLoading: Boolean = false,
    val rutina: Rutina? = null,
    val error: String? = null,
    val isDeleting: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val deleteExitoso: Boolean = false,
    val deleteError: String? = null
)

class RoutineDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val rutinaRepository: RutinaRepository
) : ViewModel() {

    private val routineId: Int = savedStateHandle.get<String>("routineId")?.toIntOrNull()
        ?: savedStateHandle.get<Int>("routineId")
        ?: 0

    private val _uiState = MutableStateFlow(RoutineDetailUiState())
    val uiState: StateFlow<RoutineDetailUiState> = _uiState.asStateFlow()

    init {
        if (routineId > 0) {
            loadRutina()
        } else {
            _uiState.update { it.copy(error = "Identificador de rutina no válido") }
        }
    }

    fun loadRutina() {
        if (routineId <= 0) return
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            rutinaRepository.getRutina(routineId)
                .onSuccess { rutina ->
                    _uiState.update { it.copy(isLoading = false, rutina = rutina, error = null) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "No se pudo cargar la rutina"
                        )
                    }
                }
        }
    }

    fun onDeleteClick() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun onConfirmDelete() {
        if (routineId <= 0) return
        _uiState.update { it.copy(isDeleting = true, showDeleteDialog = false, deleteError = null) }
        viewModelScope.launch {
            rutinaRepository.deleteRutina(routineId)
                .onSuccess {
                    _uiState.update { it.copy(isDeleting = false, deleteExitoso = true) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            deleteError = error.message ?: "No se pudo eliminar la rutina"
                        )
                    }
                }
        }
    }
}
