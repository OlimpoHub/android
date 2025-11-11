package com.app.arcabyolimpo.presentation.utils

fun validatePassword(
    password: String,
    minLength: Int = 8,
    maxLength: Int = 20,
    requireUppercase: Boolean = true,
    requireDigit: Boolean = true,
    requireSpecial: Boolean = true,
): Pair<Boolean, String?> {
    if (password.length < minLength) {
        return false to "La contraseña debe tener al menos $minLength caracteres."
    }
    if (password.length > maxLength) {
        return false to "La contraseña debe tener menos de $maxLength caracteres."
    }
    if (requireUppercase && !password.any { it.isUpperCase() }) {
        return false to "La contraseña debe contener al menos una letra mayúscula."
    }
    if (requireDigit && !password.any { it.isDigit() }) {
        return false to "La contraseña debe contener al menos un número."
    }
    if (requireSpecial && !password.any { !it.isLetterOrDigit() }) {
        return false to "La contraseña debe contener al menos un carácter especial."
    }
    if (password.contains(' ')) {
        return false to "La contraseña no debe contener espacios."
    }

    return true to null
}
