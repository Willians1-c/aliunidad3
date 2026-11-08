package com.example.gymapp.routines

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.model.GrupoMuscular
import com.example.gymapp.domain.model.Nivel
import com.example.gymapp.domain.model.Rutina
import com.example.gymapp.domain.repository.RutinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoutineFormUiState(
    val routineId: Int = 0,
    val isEditMode: Boolean = false,
    val isLoadingData: Boolean = false,
    val nombre: String = "",
    val grupoMuscular: GrupoMuscular = GrupoMuscular.PECHO,
    val series: String = "",
    val repeticiones: String = "",
    val nivel: Nivel = Nivel.PRINCIPIANTE,
    val activa: Boolean = true,
    val nombreError: String? = null,
    val seriesError: String? = null,
    val repeticionesError: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorGeneral: String? = null
)

class RoutineFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val rutinaRepository: RutinaRepository
) : ViewModel() {

    private val routineId: Int = savedStateHandle.get<String>("routineId")?.toIntOrNull()
        ?: savedStateHandle.get<Int>("routineId")
        ?: 0

    private val _uiState = MutableStateFlow(
        RoutineFormUiState(
            routineId = routineId,
            isEditMode = routineId > 0
        )
    )
    val uiState: StateFlow<RoutineFormUiState> = _uiState.asStateFlow()

    init {
        if (routineId > 0) {
            cargarRutinaParaEdicion()
        }
    }

    private fun cargarRutinaParaEdicion() {
        _uiState.update { it.copy(isLoadingData = true, errorGeneral = null) }
        viewModelScope.launch {
            rutinaRepository.getRutina(routineId)
                .onSuccess { r ->
                    _uiState.update {
                        it.copy(
                            isLoadingData = false,
                            nombre = r.nombre,
                            grupoMuscular = r.grupoMuscular,
                            series = r.series.toString(),
                            repeticiones = r.repeticiones.toString(),
                            nivel = r.nivel,
                            activa = r.activa
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoadingData = false,
                            errorGeneral = e.message ?: "No se pudo cargar la rutina para editar"
                        )
                    }
                }
        }
    }

    fun onNombreChange(value: String) =
        _uiState.update { it.copy(nombre = value, nombreError = null, errorGeneral = null) }

    fun onGrupoMuscularChange(value: GrupoMuscular) =
        _uiState.update { it.copy(grupoMuscular = value, errorGeneral = null) }

    fun onSeriesChange(value: String) =
        _uiState.update { it.copy(series = value, seriesError = null, errorGeneral = null) }

    fun onRepeticionesChange(value: String) =
        _uiState.update { it.copy(repeticiones = value, repeticionesError = null, errorGeneral = null) }

    fun onNivelChange(value: Nivel) =
        _uiState.update { it.copy(nivel = value, errorGeneral = null) }

    fun onActivaChange(value: Boolean) =
        _uiState.update { it.copy(activa = value, errorGeneral = null) }

    fun onSaveClick() {
        val s = _uiState.value
        if (s.isSaving || s.isLoadingData) return

        val nombreError = when {
            s.nombre.isBlank() -> "El nombre de la rutina es obligatorio"
            s.nombre.trim().length < 3 -> "Mínimo 3 caracteres"
            else -> null
        }

        val seriesInt = s.series.toIntOrNull()
        val seriesError = when {
            s.series.isBlank() -> "Las series son obligatorias"
            seriesInt == null || seriesInt <= 0 -> "Debe ser un número mayor a 0"
            seriesInt > 50 -> "Máximo 50 series"
            else -> null
        }

        val repeticionesInt = s.repeticiones.toIntOrNull()
        val repeticionesError = when {
            s.repeticiones.isBlank() -> "Las repeticiones son obligatorias"
            repeticionesInt == null || repeticionesInt <= 0 -> "Debe ser un número mayor a 0"
            repeticionesInt > 200 -> "Máximo 200 repeticiones"
            else -> null
        }

        if (nombreError != null || seriesError != null || repeticionesError != null) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    seriesError = seriesError,
                    repeticionesError = repeticionesError
                )
            }
            return
        }

        val rutina = Rutina(
            id = s.routineId,
            nombre = s.nombre.trim(),
            grupoMuscular = s.grupoMuscular,
            series = seriesInt!!,
            repeticiones = repeticionesInt!!,
            nivel = s.nivel,
            activa = s.activa
        )

        _uiState.update { it.copy(isSaving = true, errorGeneral = null) }
        viewModelScope.launch {
            val resultado = if (s.isEditMode) {
                rutinaRepository.updateRutina(rutina)
            } else {
                rutinaRepository.createRutina(rutina)
            }

            resultado
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorGeneral = e.message ?: "No se pudo guardar la rutina"
                        )
                    }
                }
        }
    }
}
