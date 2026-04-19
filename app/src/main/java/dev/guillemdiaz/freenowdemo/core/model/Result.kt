package dev.guillemdiaz.freenowdemo.core.model

/**
 * A generic wrapper for async operation outcomes across the data layer.
 * - [Success] carries the result payload.
 * - [Error] carries the exception that caused the failure.
 * - [Loading] represents an in-progress operation with no data yet.
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
}
