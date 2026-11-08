package com.example.gymapp.data.mapper



import com.example.gymapp.data.remote.dto.UsuarioDto
import com.example.gymapp.domain.model.Usuario

fun UsuarioDto.toDomain(): Usuario = Usuario(
    id = id,
    nombre = nombre,
    correo = correo
)