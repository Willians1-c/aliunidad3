package com.example.gymapp.domain.repository



import com.example.gymapp.domain.model.Usuario

interface AuthRepository {
    suspend fun register(nombre: String, correo: String, password: String): Result<Usuario>
    suspend fun login(correo: String, password: String): Result<Usuario>
    fun hasSession(): Boolean
    fun logout()
}