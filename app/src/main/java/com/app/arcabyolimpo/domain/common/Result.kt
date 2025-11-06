package com.app.arcabyolimpo.domain.common

/**
 * A generic sealed class used to represent the state of an operation.
 *
 * It can be in one of three states:
 * - [Loading]: The operation is currently in progress.
 * - [Success]: The operation completed successfully with a result.
 * - [Error]: The operation failed with an exception.
 *
 * Used to emit UI state from ViewModels or UseCases.
 */
sealed class Result<out T> {
    /** Represents a loading or in-progress state. */
    object Loading : Result<Nothing>()

    /** Represents a successful result containing [data]. */
    data class Success<T>(
        val data: T,
    ) : Result<T>()

    /** Represents a failed result containing an [exception]. */
    data class Error(
        val exception: Throwable,
    ) : Result<Nothing>()
}
