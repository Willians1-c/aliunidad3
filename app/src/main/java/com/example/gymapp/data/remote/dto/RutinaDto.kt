package com.example.gymapp.data.remote.dto



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RutinaDto(
    val id: Int? = null,                       // al crear, aún no tiene id
    val nombre: String,
    @SerialName("grupo_muscular") val grupoMuscular: String,
    val series: Int,
    val repeticiones: Int,
    val nivel: String,
    val activa: Boolean
)