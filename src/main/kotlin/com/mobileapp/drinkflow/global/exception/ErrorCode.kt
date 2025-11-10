package com.mobileapp.drinkflow.global.exception

class CustomException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message
) : RuntimeException(message)

enum class ErrorCode(
    val status: Int,
    val message: String
) {
    // Auth & User
    UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),

    // Common
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(400, "잘못된 요청입니다.");

    fun toException(): CustomException {
        return CustomException(this)
    }

    fun toException(message: String?): CustomException {
        return CustomException(this, message ?: this.message)
    }
}

