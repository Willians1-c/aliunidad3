package com.example.gymapp.domain.model
data class Rutina(
    val id: Int = 0,               // 0 = rutina nueva, aún sin id del servidor
    val nombre: String,
    val grupoMuscular: GrupoMuscular,
    val series: Int,
    val repeticiones: Int,
    val nivel: Nivel,
    val activa: Boolean
)