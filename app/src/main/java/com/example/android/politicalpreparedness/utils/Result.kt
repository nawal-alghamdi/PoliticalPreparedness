package com.example.android.politicalpreparedness.utils

/**
 * @Author: nawalalghamdi
 * @Date: 14/09/2023
 */
/**
 * A sealed class that encapsulates successful outcome with a value of type [T]
 * or a failure with message and statusCode
 */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val message: String?, val statusCode: Int? = null) :
        Result<Nothing>()
}