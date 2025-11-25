package com.mobileapp.drinkflow.domain.user.controller

import com.mobileapp.drinkflow.core.jwt.JwtPrincipal
import com.mobileapp.drinkflow.domain.drinkRecord.dto.DailyIntakeRankingPageResponse
import com.mobileapp.drinkflow.domain.drinkRecord.service.DrinkRecordService
import com.mobileapp.drinkflow.domain.user.dto.UserDetailResponse
import com.mobileapp.drinkflow.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val drinkRecordService: DrinkRecordService
) {

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal jwtPrincipal: JwtPrincipal): ResponseEntity<UserDetailResponse> {
        return ResponseEntity.ok(userService.findById(jwtPrincipal.id))
    }

    @GetMapping("/today-rankings")
    @Operation(description = "금일 물 섭취량 순위를 조회합니다. 동점자는 같은 순위로 표기됩니다.")
    fun getTodayRanking(): ResponseEntity<DailyIntakeRankingPageResponse> {
        val response = drinkRecordService.getTodayRanking()
        return ResponseEntity.ok(response)
    }
}