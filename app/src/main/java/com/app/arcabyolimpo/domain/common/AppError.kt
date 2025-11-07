package com.app.arcabyolimpo.domain.common

/**
 * Represents different types of application-level errors
 * that can occur during domain or data operations.
 */
sealed class AppError(
    message: String,
) : Exception(message) {
    /** Thrown when authentication fails due to invalid credentials (HTTP 401). */
    class Unauthorized : AppError("Credenciales inválidas")

    /** Thrown when a server-side error occurs (HTTP 5xx). */
    class ServerError : AppError("Error del servidor")

    /** Thrown when the device has no internet connection. */
    class NetworkError : AppError("Sin conexión a Internet")

    /** Thrown for any unexpected or uncategorized error. */
    class Unknown(
        message: String,
    ) : AppError(message)
}
