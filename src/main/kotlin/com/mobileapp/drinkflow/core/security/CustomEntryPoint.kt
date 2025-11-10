package com.mobileapp.drinkflow.core.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mobileapp.drinkflow.global.exception.ErrorCode
import com.mobileapp.drinkflow.global.exception.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.info("authException.getMessage() == {}", authException.message)

        val error = ErrorResponse(ErrorCode.UNAUTHORIZED.toException(authException.message))
        response.contentType = "application/json;charset=UTF-8"
        response.status = error.status
        response.writer.use { w ->
            w.write(objectMapper.writeValueAsString(error))
        }
    }

    @Throws(IOException::class)
    fun commenceExpiredToken(response: HttpServletResponse) {
        val error = ErrorResponse(ErrorCode.EXPIRED_TOKEN.toException("access token expired"))
        response.contentType = "application/json;charset=UTF-8"
        response.status = error.status
        response.writer.use { w ->
            w.write(objectMapper.writeValueAsString(error))
        }
    }
}
