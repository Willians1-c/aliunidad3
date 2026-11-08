package com.example.gymapp.data.repository



import com.example.gymapp.data.mapper.toDomain
import com.example.gymapp.data.remote.api.AuthApi
import com.example.gymapp.data.remote.dto.LoginRequestDto
import com.example.gymapp.data.remote.dto.RegisterRequestDto
import com.example.gymapp.domain.model.Usuario
import com.example.gymapp.domain.repository.AuthRepository
import com.example.gymapp.domain.repository.SessionStorage

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val session: SessionStorage
) : AuthRepository {

    override suspend fun register(nombre: String, correo: String, password: String): Result<Usuario> =
        apiCall {
            val response = api.register(RegisterRequestDto(nombre, correo, password))
            session.saveToken(response.token)
            response.user.toDomain()
        }

    override suspend fun login(correo: String, password: String): Result<Usuario> =
        apiCall {
            val response = api.login(LoginRequestDto(correo, password))
            session.saveToken(response.token)
            response.user.toDomain()
        }

    override fun hasSession(): Boolean = session.getToken() != null

    override fun logout() = session.clear()
}