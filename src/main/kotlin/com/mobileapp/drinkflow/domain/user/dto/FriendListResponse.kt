package com.mobileapp.drinkflow.domain.user.dto

data class FriendListResponse(
    val friends: List<UserDetailResponse>
)

data class FriendResponse(
    val id: Long?,
    val name: String?,
    val username: String?
)

