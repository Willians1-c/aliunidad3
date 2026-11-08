package com.example.gymapp.data.repository


import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ApiException(message: String) : Exception(message)

/** Ejecuta una llamada de red y devuelve Result con un mensaje de error legible. */
suspend fun <T> apiCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e // nunca se debe tragar la cancelación de una corrutina
    } catch (e: Exception) {
        Result.failure(ApiException(e.toMensaje()))
    }

private fun Throwable.toMensaje(): String = when (this) {
    is HttpException -> detalleDelServidor() ?: when (code()) {
        401 -> "Sesión no válida. Inicia sesión de nuevo"
        404 -> "No se encontró el recurso"
        in 500..599 -> "Error en el servidor"
        else -> "Error de red (${code()})"
    }
    is IOException -> "No se pudo conectar con el servidor"
    else -> "Ocurrió un error inesperado"
}

private fun HttpException.detalleDelServidor(): String? {
    return try {
        val body = response()?.errorBody()?.string() ?: return null
        val detail = Json.parseToJsonElement(body).jsonObject["detail"]
        // 401, 404 y 409 traen texto; un 422 trae una lista de errores de validación
        if (detail is JsonPrimitive && detail.isString) detail.content
        else "Revisa los datos ingresados"
    } catch (e: Exception) {
        null
    }
}