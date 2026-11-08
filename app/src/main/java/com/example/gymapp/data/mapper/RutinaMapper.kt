package com.example.gymapp.data.mapper



import com.example.gymapp.data.remote.dto.RutinaDto
import com.example.gymapp.domain.model.GrupoMuscular
import com.example.gymapp.domain.model.Nivel
import com.example.gymapp.domain.model.Rutina

fun RutinaDto.toDomain(): Rutina = Rutina(
    id = id ?: 0,
    nombre = nombre,
    grupoMuscular = GrupoMuscular.entries.first { it.etiqueta == grupoMuscular },
    series = series,
    repeticiones = repeticiones,
    nivel = Nivel.entries.first { it.etiqueta == nivel },
    activa = activa
)

fun Rutina.toDto(): RutinaDto = RutinaDto(
    id = if (id == 0) null else id,
    nombre = nombre,
    grupoMuscular = grupoMuscular.etiqueta,
    series = series,
    repeticiones = repeticiones,
    nivel = nivel.etiqueta,
    activa = activa
)