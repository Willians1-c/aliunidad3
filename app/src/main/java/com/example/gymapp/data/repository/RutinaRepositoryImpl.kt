package com.example.gymapp.data.repository



import com.example.gymapp.data.mapper.toDomain
import com.example.gymapp.data.mapper.toDto
import com.example.gymapp.data.remote.api.RutinaApi
import com.example.gymapp.domain.model.Rutina
import com.example.gymapp.domain.repository.RutinaRepository

class RutinaRepositoryImpl(private val api: RutinaApi) : RutinaRepository {

    override suspend fun getRutinas(): Result<List<Rutina>> =
        apiCall { api.getRutinas().map { it.toDomain() } }

    override suspend fun getRutina(id: Int): Result<Rutina> =
        apiCall { api.getRutina(id).toDomain() }

    override suspend fun createRutina(rutina: Rutina): Result<Rutina> =
        apiCall { api.createRutina(rutina.toDto()).toDomain() }

    override suspend fun updateRutina(rutina: Rutina): Result<Rutina> =
        apiCall { api.updateRutina(rutina.id, rutina.toDto()).toDomain() }

    override suspend fun deleteRutina(id: Int): Result<Unit> =
        apiCall { api.deleteRutina(id) }
}