package com.mobileapp.drinkflow.domain.user.repository

import com.mobileapp.drinkflow.domain.user.entity.Friendship
import com.mobileapp.drinkflow.domain.user.entity.FriendshipStatus
import com.mobileapp.drinkflow.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FriendshipRepository : JpaRepository<Friendship, Long> {

    // 특정 사용자의 수락된 친구 목록 조회
    @Query("SELECT f FROM Friendship f JOIN FETCH f.friend WHERE f.user.id = :userId AND f.status = :status")
    fun findByUserIdAndStatus(
        @Param("userId") userId: Long,
        @Param("status") status: FriendshipStatus
    ): List<Friendship>

    // 특정 사용자가 받은 친구 요청 조회 (다른 사람이 나에게 보낸 요청)
    @Query("SELECT f FROM Friendship f JOIN FETCH f.user WHERE f.friend.id = :userId AND f.status = 'PENDING'")
    fun findPendingRequestsReceivedByUser(@Param("userId") userId: Long): List<Friendship>

    // 특정 사용자가 보낸 친구 요청 조회
    @Query("SELECT f FROM Friendship f JOIN FETCH f.friend WHERE f.user.id = :userId AND f.status = 'PENDING'")
    fun findPendingRequestsSentByUser(@Param("userId") userId: Long): List<Friendship>

    // 두 사용자 간의 친구 관계 조회
    @Query("SELECT f FROM Friendship f WHERE (f.user.id = :userId AND f.friend.id = :friendId) OR (f.user.id = :friendId AND f.friend.id = :userId)")
    fun findFriendshipBetweenUsers(
        @Param("userId") userId: Long,
        @Param("friendId") friendId: Long
    ): List<Friendship>

    // 특정 친구 관계가 이미 존재하는지 확인 (중복 방지)
    fun existsByUserAndFriend(user: User, friend: User): Boolean

    // ID로 친구 관계 조회 with fetch join
    @Query("SELECT f FROM Friendship f JOIN FETCH f.user JOIN FETCH f.friend WHERE f.id = :id")
    fun findByIdWithUserAndFriend(@Param("id") id: Long): Friendship?
}

