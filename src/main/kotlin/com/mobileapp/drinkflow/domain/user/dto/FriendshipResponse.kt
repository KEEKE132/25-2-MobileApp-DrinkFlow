package com.mobileapp.drinkflow.domain.user.dto

import com.mobileapp.drinkflow.domain.user.entity.FriendshipStatus

data class FriendshipResponse(
    val id: Long?,
    val userId: Long?,
    val friendId: Long?,
    val friendName: String?,
    val friendUsername: String?,
    val status: FriendshipStatus
)

