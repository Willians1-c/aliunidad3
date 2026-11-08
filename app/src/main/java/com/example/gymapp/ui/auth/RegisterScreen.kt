package com.example.gymapp.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.gymapp.ui.AppViewModelProvider
import com.example.gymapp.ui.components.ErrorBanner
import com.example.gymapp.ui.components.FormTextField
import com.example.gymapp.ui.components.LoadingButton

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.registroExitoso) {
        if (state.registroExitoso) onRegisterSuccess()
    }

    RegisterContent(
        state = state,
        onNombreChange = viewModel::onNombreChange,
        onCorreoChange = viewModel::onCorreoChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmacionChange = viewModel::onConfirmacionChange,
        onRegisterClick = viewModel::onRegisterClick,
        onGoToLogin = onGoToLogin
    )
}

@Composable
private fun RegisterContent(
    state: RegisterUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmacionChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onGoToLogin: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Crea tu cuenta", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "Regístrate para guardar tus rutinas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))

            state.errorGeneral?.let { ErrorBanner(it) }

            FormTextField(
                label = "Nombre",
                value = state.nombre,
                onValueChange = onNombreChange,
                error = state.nombreError,
                enabled = !state.isLoading
            )
            FormTextField(
                label = "Correo electrónico",
                value = state.correo,
                onValueChange = onCorreoChange,
                error = state.correoError,
                keyboardType = KeyboardType.Email,
                enabled = !state.isLoading
            )
            FormTextField(
                label = "Contraseña",
                value = state.password,
                onValueChange = onPasswordChange,
                error = state.passwordError,
                isPassword = true,
                enabled = !state.isLoading
            )
            FormTextField(
                label = "Confirmar contraseña",
                value = state.confirmacion,
                onValueChange = onConfirmacionChange,
                error = state.confirmacionError,
                isPassword = true,
                imeAction = ImeAction.Done,
                enabled = !state.isLoading
            )

            Spacer(Modifier.height(4.dp))
            LoadingButton(text = "Crear cuenta", onClick = onRegisterClick, isLoading = state.isLoading)

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes cuenta?", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onGoToLogin) { Text("Inicia sesión") }
            }
        }
    }
}