package com.alazhartex.costing.domain.engine

/** Minimal sealed result type so the domain has no Android / third-party dependency. */
sealed interface Result<out T, out E> {
    data class Success<T>(val value: T) : Result<T, Nothing>
    data class Failure<E>(val error: E) : Result<Nothing, E>

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun <R> map(transform: (T) -> R): Result<R, E> = when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }
}
