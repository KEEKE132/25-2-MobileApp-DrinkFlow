package com.mobileapp.drinkflow.global.exception

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Custom Exception
     */
    @ExceptionHandler(CustomException::class)
    protected fun handleCustomException(e: CustomException): ResponseEntity<ErrorResponse> {
        log.debug("handleCustomException: {}", e.errorCode)
        return ResponseEntity
            .status(e.errorCode.status)
            .body(ErrorResponse(e))
    }

    /**
     * 유효성 검사 오류 메시지 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        log.debug("handleValidationExceptions: {}", e.message)
        val errors = e.bindingResult.fieldErrors
            .associate { error: FieldError ->
                error.field to error.defaultMessage
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }

    /**
     * 인증은 되었지만 권한이 없는 경우
     */
    @ExceptionHandler(AccessDeniedException::class)
    protected fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ErrorResponse> {
        log.debug("handleAccessDeniedException: {}", e.message)
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ErrorResponse(ErrorCode.AUTHORITY_FORBIDDEN.toException()))
    }

    /**
     * 인증 자체가 실패한 경우 (로그인 안 됨, 토큰 만료 등)
     */
    @ExceptionHandler(AuthenticationException::class)
    protected fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<ErrorResponse> {
        log.debug("handleAuthenticationException: {}", e.message)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse(ErrorCode.UNAUTHORIZED.toException()))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    protected fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        log.debug("handleDataIntegrityViolationException: {}", e.cause?.message)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(ErrorCode.DATA_CONFLICT.toException()))
    }

    /**
     * HTTP 404 잘못된 api 경로
     */
    @ExceptionHandler(NoHandlerFoundException::class, NoResourceFoundException::class)
    protected fun handleNoHandlerFoundException(e: Exception): ResponseEntity<ErrorResponse> {
        log.trace("handleNoHandlerFoundException: {}", e.message)
        return ResponseEntity
            .status(ErrorCode.API_NOT_FOUND.status)
            .body(ErrorResponse(ErrorCode.API_NOT_FOUND.toException()))
    }

    /**
     * HTTP 400 쿼리 파라미터가 없는 경우
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    protected fun handleMissingParameterException(e: Exception): ResponseEntity<ErrorResponse> {
        log.trace("handleMissingParameterException: {}", e.message)
        return ResponseEntity
            .status(ErrorCode.BAD_REQUEST.status)
            .body(ErrorResponse(ErrorCode.BAD_REQUEST.toException(e.message)))
    }

    /**
     * HTTP 400 파라미터 타입이 맞지 않는 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleTypeMismatchException(e: Exception): ResponseEntity<ErrorResponse> {
        log.trace("handleTypeMismatchException: {}", e.message)
        return ResponseEntity
            .status(ErrorCode.BAD_REQUEST.status)
            .body(ErrorResponse(ErrorCode.BAD_REQUEST.toException("타입이 올바르지 않습니다.")))
    }

    /**
     * HTTP 500 Exception
     */
    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("handleException: {}", e.message, e)
        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.status)
            .body(ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.toException()))
    }
}