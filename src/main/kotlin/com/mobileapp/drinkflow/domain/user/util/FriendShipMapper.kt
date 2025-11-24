package com.mobileapp.drinkflow.domain.user.util

import com.mobileapp.drinkflow.domain.user.dto.FriendListResponse
import com.mobileapp.drinkflow.domain.user.dto.FriendResponse
import com.mobileapp.drinkflow.domain.user.dto.FriendshipResponse
import com.mobileapp.drinkflow.domain.user.entity.Friendship
import com.mobileapp.drinkflow.domain.user.entity.User

object FriendShipMapper {

    /**
     * Friendship 엔티티를 FriendshipResponse로 변환
     * @param friendship 친구 관계 엔티티
     */
    fun toFriendshipResponse(friendship: Friendship): FriendshipResponse {
        return FriendshipResponse(
            id = friendship.id,
            userId = friendship.user.id,
            friendId = friendship.friend.id,
            friendName = friendship.friend.name,
            friendUsername = friendship.friend.username,
            status = friendship.status
        )
    }

    /**
     * Friendship 엔티티를 FriendshipResponse로 변환 (받은 요청용)
     * friendName과 friendUsername을 요청을 보낸 사람(user)으로 설정
     */
    fun toReceivedFriendshipResponse(friendship: Friendship): FriendshipResponse {
        return FriendshipResponse(
            id = friendship.id,
            userId = friendship.user.id,
            friendId = friendship.friend.id,
            friendName = friendship.user.name,
            friendUsername = friendship.user.username,
            status = friendship.status
        )
    }

    /**
     * User 엔티티를 FriendResponse로 변환
     */
    fun toFriendResponse(user: User): FriendResponse {
        return FriendResponse(
            id = user.id,
            name = user.name,
            username = user.username
        )
    }

    /**
     * Friendship 리스트를 FriendResponse 리스트로 변환 (친구 목록용)
     */
    fun toFriendResponseList(friendships: List<Friendship>): List<FriendResponse> {
        return friendships.map { friendship ->
            FriendResponse(
                id = friendship.friend.id,
                name = friendship.friend.name,
                username = friendship.friend.username
            )
        }
    }

    /**
     * FriendResponse 리스트를 FriendListResponse로 변환
     */
    fun toFriendListResponse(friends: List<FriendResponse>): FriendListResponse {
        return FriendListResponse(friends = friends)
    }

    /**
     * Friendship 리스트를 FriendshipResponse 리스트로 변환
     */
    fun toFriendshipResponseList(friendships: List<Friendship>): List<FriendshipResponse> {
        return friendships.map { toFriendshipResponse(it) }
    }

    /**
     * Friendship 리스트를 FriendshipResponse 리스트로 변환 (받은 요청용)
     */
    fun toReceivedFriendshipResponseList(friendships: List<Friendship>): List<FriendshipResponse> {
        return friendships.map { toReceivedFriendshipResponse(it) }
    }
}

