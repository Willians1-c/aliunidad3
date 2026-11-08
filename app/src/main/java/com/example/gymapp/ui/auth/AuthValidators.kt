package com.example.gymapp.ui.auth

object AuthValidators {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    const val MIN_PASSWORD = 8

    fun nombre(value: String): String? = when {
        value.isBlank() -> "El nombre es obligatorio"
        value.trim().length < 2 -> "Mínimo 2 caracteres"
        else -> null
    }

    fun correo(value: String): String? = when {
        value.isBlank() -> "El correo es obligatorio"
        !EMAIL_REGEX.matches(value.trim()) -> "Escribe un correo válido"
        else -> null
    }

    fun password(value: String): String? = when {
        value.isBlank() -> "La contraseña es obligatoria"
        value.length < MIN_PASSWORD -> "Mínimo $MIN_PASSWORD caracteres"
        else -> null
    }

    fun confirmacion(password: String, confirmacion: String): String? = when {
        confirmacion.isBlank() -> "Confirma tu contraseña"
        password != confirmacion -> "Las contraseñas no coinciden"
        else -> null
    }
}