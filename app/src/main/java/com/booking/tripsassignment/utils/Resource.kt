package com.booking.tripsassignment.utils

/**
 * A Result implementation class.
 * It's a sealed class which supports two types: Success And Error.
 */
sealed class Resource<out R> {
    object Loading : Resource<Nothing>()
    data class Success<out R>(val data: R) : Resource<R>()
    data class Error(val exception: Throwable) : Resource<Nothing>()

    /**
     * Get the result data if it is Success else null.
     */
    fun data(): R? {
        return when (this) {
            is Success -> data
            else -> null
        }
    }
}