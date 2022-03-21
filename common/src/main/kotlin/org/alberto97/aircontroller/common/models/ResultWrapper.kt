package org.alberto97.aircontroller.common.models

sealed class ResultWrapper<T>(
    val data: T? = null,
    val code: Int? = null,
    val message: String = ""
) {
    class Success<T>(data: T) : ResultWrapper<T>(data)
    //class Loading<T>(data: T? = null) : ResultWrapper<T>(data)
    class Error<T>(message: String, code: Int? = null, data: T? = null) : ResultWrapper<T>(data, code, message)
}

object DefaultErrors {
    fun <T> connectivityError(): ResultWrapper.Error<T> {
        return ResultWrapper.Error("Check your connectivity and try again")
    }
}