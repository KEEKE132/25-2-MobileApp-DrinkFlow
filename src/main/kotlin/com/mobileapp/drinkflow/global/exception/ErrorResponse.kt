package com.mobileapp.drinkflow.global.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    constructor(exception: CustomException) : this(
        status = exception.errorCode.status,
        message = exception.message
    )
}

