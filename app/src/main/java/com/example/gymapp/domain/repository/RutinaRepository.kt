package com.example.gymapp.domain.repository



import com.example.gymapp.domain.model.Rutina

interface RutinaRepository {
    suspend fun getRutinas(): Result<List<Rutina>>
    suspend fun getRutina(id: Int): Result<Rutina>
    suspend fun createRutina(rutina: Rutina): Result<Rutina>
    suspend fun updateRutina(rutina: Rutina): Result<Rutina>
    suspend fun deleteRutina(id: Int): Result<Unit>
}