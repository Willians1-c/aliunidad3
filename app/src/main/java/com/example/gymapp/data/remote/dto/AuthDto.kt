package com.example.gymapp.data.remote.dto



import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val nombre: String,
    val correo: String,
    val password: String
)

@Serializable
data class LoginRequestDto(
    val correo: String,
    val password: String
)

@Serializable
data class UsuarioDto(
    val id: Int,
    val nombre: String,
    val correo: String
)

@Serializable
data class AuthResponseDto(
    val token: String,
    val user: UsuarioDto
)