package com.mobileapp.drinkflow.domain.user.dto

data class FriendListResponse(
    val friends: List<FriendResponse>
)

data class FriendResponse(
    val id: Long?,
    val name: String?,
    val username: String?
)

