package com.app.arcabyolimpo.presentation.util

/**
 * Validates that the input string follows a basic email pattern.
 *
 * @param input the email string to validate
 * @return true if the email is valid, false otherwise
 */
fun validateEmail(input: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z0-9.-]+\$".toRegex()
    return input.matches(emailRegex)
}
