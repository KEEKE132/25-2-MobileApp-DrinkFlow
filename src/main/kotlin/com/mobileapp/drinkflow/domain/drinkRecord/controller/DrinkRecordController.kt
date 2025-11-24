package com.mobileapp.drinkflow.domain.drinkRecord.controller

import com.mobileapp.drinkflow.core.jwt.JwtPrincipal
import com.mobileapp.drinkflow.domain.drinkRecord.dto.DrinkRecordPageResponse
import com.mobileapp.drinkflow.domain.drinkRecord.dto.DrinkRecordRequest
import com.mobileapp.drinkflow.domain.drinkRecord.dto.DrinkRecordResponse
import com.mobileapp.drinkflow.domain.drinkRecord.service.DrinkRecordService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/drink-records")
class DrinkRecordController(
    private val drinkRecordService: DrinkRecordService
) {

    @PostMapping
    fun create(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @Valid @RequestBody request: DrinkRecordRequest
    ): ResponseEntity<DrinkRecordResponse> {
        val response = drinkRecordService.create(principal.id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<DrinkRecordResponse> {
        val response = drinkRecordService.get(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    @Operation(
        description =
            """
    userId가 없으면 현재 로그인한 유저 정보 사용
    startDate나 endDate 중 하나라도 없으면 전체기간 조회
    """
    )
    fun list(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false)
        startDate: LocalDateTime?,
        @RequestParam(required = false)
        endDate: LocalDateTime?,
        @RequestParam(required = false) userId: Long?,
    ): ResponseEntity<DrinkRecordPageResponse> {

        val targetUserId = userId ?: principal.id
        val response = drinkRecordService.list(targetUserId, page, size, startDate, endDate)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{id}")
    fun update(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @PathVariable id: Long,
        @Valid @RequestBody request: DrinkRecordRequest
    ): ResponseEntity<DrinkRecordResponse> {
        val response = drinkRecordService.update(principal.id, id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal principal: JwtPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        drinkRecordService.delete(principal.id, id)
        return ResponseEntity.noContent().build()
    }
}

