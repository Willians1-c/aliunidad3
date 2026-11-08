package com.example.gymapp.routines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.domain.model.GrupoMuscular
import com.example.gymapp.domain.model.Nivel
import com.example.gymapp.ui.AppViewModelProvider
import com.example.gymapp.ui.components.ErrorBanner
import com.example.gymapp.ui.components.FormTextField
import com.example.gymapp.ui.components.LoadingButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineFormScreen(
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: RoutineFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            onSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state.isEditMode) "Editar Rutina" else "Nueva Rutina")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoadingData) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.errorGeneral?.let { ErrorBanner(it) }

                FormTextField(
                    label = "Nombre de la rutina",
                    value = state.nombre,
                    onValueChange = viewModel::onNombreChange,
                    error = state.nombreError,
                    enabled = !state.isSaving
                )

                Text(
                    text = "Grupo Muscular",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GrupoMuscular.entries.forEach { grupo ->
                        FilterChip(
                            selected = state.grupoMuscular == grupo,
                            onClick = { viewModel.onGrupoMuscularChange(grupo) },
                            label = { Text(grupo.etiqueta) },
                            enabled = !state.isSaving
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FormTextField(
                        label = "Series",
                        value = state.series,
                        onValueChange = viewModel::onSeriesChange,
                        error = state.seriesError,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        enabled = !state.isSaving
                    )
                    FormTextField(
                        label = "Repeticiones",
                        value = state.repeticiones,
                        onValueChange = viewModel::onRepeticionesChange,
                        error = state.repeticionesError,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        modifier = Modifier.weight(1f),
                        enabled = !state.isSaving
                    )
                }

                Text(
                    text = "Nivel de dificultad",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Nivel.entries.forEach { nivel ->
                        FilterChip(
                            selected = state.nivel == nivel,
                            onClick = { viewModel.onNivelChange(nivel) },
                            label = { Text(nivel.etiqueta) },
                            enabled = !state.isSaving
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rutina Activa",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Disponible en tu lista de entrenamiento activo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = state.activa,
                        onCheckedChange = viewModel::onActivaChange,
                        enabled = !state.isSaving
                    )
                }

                Spacer(Modifier.height(8.dp))

                LoadingButton(
                    text = if (state.isEditMode) "Guardar cambios" else "Crear rutina",
                    onClick = viewModel::onSaveClick,
                    isLoading = state.isSaving
                )
            }
        }
    }
}
