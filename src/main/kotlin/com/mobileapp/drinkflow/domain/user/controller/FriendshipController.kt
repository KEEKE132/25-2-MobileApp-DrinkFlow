package com.mobileapp.drinkflow.domain.user.controller

import com.mobileapp.drinkflow.core.jwt.JwtPrincipal
import com.mobileapp.drinkflow.domain.user.dto.FriendListResponse
import com.mobileapp.drinkflow.domain.user.dto.FriendResponse
import com.mobileapp.drinkflow.domain.user.dto.FriendshipRequest
import com.mobileapp.drinkflow.domain.user.dto.FriendshipResponse
import com.mobileapp.drinkflow.domain.user.service.FriendShipService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/friends")
class FriendshipController(
    private val friendShipService: FriendShipService
) {

    @Operation(summary = "친구 요청 보내기", description = "다른 사용자에게 친구 요청을 보냅니다.")
    @PostMapping
    fun sendFriendRequest(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal,
        @RequestBody request: FriendshipRequest
    ): ResponseEntity<FriendshipResponse> {
        val response = friendShipService.sendFriendRequest(jwtPrincipal.id, request.friendUsername)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "친구 요청 수락", description = "받은 친구 요청을 수락하고 양방향 친구 관계를 생성합니다.")
    @PatchMapping("/requests/{friendshipId}")
    fun acceptFriendRequest(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal,
        @PathVariable friendshipId: Long
    ): ResponseEntity<FriendshipResponse> {
        val response = friendShipService.acceptFriendRequest(jwtPrincipal.id, friendshipId)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "친구 요청 거절", description = "받은 친구 요청을 거절합니다.")
    @DeleteMapping("/requests/{friendshipId}")
    fun rejectFriendRequest(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal,
        @PathVariable friendshipId: Long
    ): ResponseEntity<Void> {
        friendShipService.rejectFriendRequest(jwtPrincipal.id, friendshipId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "친구 차단", description = "특정 사용자를 차단합니다. 기존 친구 관계가 있다면 삭제됩니다.")
    @PostMapping("/{friendId}/block")
    fun blockFriend(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal,
        @PathVariable friendId: Long
    ): ResponseEntity<FriendshipResponse> {
        val response = friendShipService.blockFriend(jwtPrincipal.id, friendId)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "친구 삭제", description = "친구 관계를 끊습니다. 양방향 친구 관계가 모두 삭제됩니다.")
    @DeleteMapping("/{friendId}")
    fun removeFriend(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal,
        @PathVariable friendId: Long
    ): ResponseEntity<Void> {
        friendShipService.removeFriend(jwtPrincipal.id, friendId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "내 친구 목록 조회", description = "현재 로그인한 사용자의 친구 목록을 조회합니다.")
    @GetMapping
    fun getMyFriends(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal
    ): ResponseEntity<FriendListResponse> {
        val response = friendShipService.getMyFriends(jwtPrincipal.id)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "받은 친구 요청 목록 조회", description = "다른 사용자로부터 받은 친구 요청 목록을 조회합니다.")
    @GetMapping("/requests/received")
    fun getReceivedFriendRequests(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal
    ): ResponseEntity<List<FriendshipResponse>> {
        val response = friendShipService.getReceivedFriendRequests(jwtPrincipal.id)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "보낸 친구 요청 목록 조회", description = "내가 다른 사용자에게 보낸 친구 요청 목록을 조회합니다.")
    @GetMapping("/requests/sent")
    fun getSentFriendRequests(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal
    ): ResponseEntity<List<FriendshipResponse>> {
        val response = friendShipService.getSentFriendRequests(jwtPrincipal.id)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "차단 목록 조회", description = "내가 차단한 사용자 목록을 조회합니다.")
    @GetMapping("/blocked")
    fun getBlockedUsers(
        @AuthenticationPrincipal jwtPrincipal: JwtPrincipal
    ): ResponseEntity<List<FriendResponse>> {
        val response = friendShipService.getBlockedUsers(jwtPrincipal.id)
        return ResponseEntity.ok(response)
    }
}

