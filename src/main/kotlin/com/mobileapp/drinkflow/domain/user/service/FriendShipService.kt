package com.mobileapp.drinkflow.domain.user.service

import com.mobileapp.drinkflow.domain.user.dto.FriendListResponse
import com.mobileapp.drinkflow.domain.user.dto.FriendResponse
import com.mobileapp.drinkflow.domain.user.dto.FriendshipResponse
import com.mobileapp.drinkflow.domain.user.entity.Friendship
import com.mobileapp.drinkflow.domain.user.entity.FriendshipStatus
import com.mobileapp.drinkflow.domain.user.repository.FriendshipRepository
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.domain.user.util.FriendShipMapper
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendShipService(
    private val friendshipRepository: FriendshipRepository,
    private val userRepository: UserRepository
) {

    /**
     * 친구 요청 보내기
     */
    @Transactional
    fun sendFriendRequest(userId: Long, friendUsername: String): FriendshipResponse {
        val user =
            userRepository.findByIdOrNull(userId) ?: throw ErrorCode.USER_NOT_FOUND.toException()
        // 자기 자신에게 친구 요청을 보낼 수 없음
        if (user.username == friendUsername) {
            throw ErrorCode.CANNOT_ADD_SELF_AS_FRIEND.toException()
        }
        val friend = userRepository.findByUsername(friendUsername)
            ?: throw ErrorCode.USER_NOT_FOUND.toException()

        // 이미 친구 관계가 존재하는지 확인
        val existingFriendships =
            friendshipRepository.findFriendshipBetweenUsers(userId, friend.id!!)
        if (existingFriendships.isNotEmpty()) {
            existingFriendships.forEach { e ->
                if (e.status == FriendshipStatus.BLOCKED) {
                    throw ErrorCode.FRIENDSHIP_BLOCKED.toException()
                }
            }
            throw ErrorCode.FRIENDSHIP_ALREADY_EXISTS.toException()
        }

        // 친구 요청 생성
        val friendship = Friendship(
            user = user,
            friend = friend,
            status = FriendshipStatus.PENDING
        )

        val savedFriendship = friendshipRepository.save(friendship)

        return FriendShipMapper.toFriendshipResponse(savedFriendship)
    }

    /**
     * 친구 요청 수락
     */
    @Transactional
    fun acceptFriendRequest(userId: Long, friendshipId: Long): FriendshipResponse {
        val friendship = friendshipRepository.findByIdWithUserAndFriend(friendshipId)
            ?: throw ErrorCode.FRIENDSHIP_NOT_FOUND.toException()

        // 요청을 받은 사람만 수락할 수 있음
        if (friendship.friend.id != userId) {
            throw ErrorCode.NOT_AUTHORIZED_TO_RESPOND.toException()
        }

        // PENDING 상태인지 확인
        if (friendship.status != FriendshipStatus.PENDING) {
            throw ErrorCode.FRIENDSHIP_NOT_PENDING.toException()
        }

        // 상태를 ACCEPTED로 변경
        friendship.status = FriendshipStatus.ACCEPTED

        // 양방향 친구 관계 생성 (상대방도 내 친구 목록에서 볼 수 있도록)
        val reverseFriendship = Friendship(
            user = friendship.friend,
            friend = friendship.user,
            status = FriendshipStatus.ACCEPTED
        )
        friendshipRepository.save(reverseFriendship)

        return FriendShipMapper.toFriendshipResponse(friendship)
    }

    /**
     * 친구 요청 거절
     */
    @Transactional
    fun rejectFriendRequest(userId: Long, friendshipId: Long) {
        val friendship = friendshipRepository.findByIdWithUserAndFriend(friendshipId)
            ?: throw ErrorCode.FRIENDSHIP_NOT_FOUND.toException()

        // 요청을 받은 사람만 거절할 수 있음
        if (friendship.friend.id != userId) {
            throw ErrorCode.NOT_AUTHORIZED_TO_RESPOND.toException()
        }

        // PENDING 상태인지 확인
        if (friendship.status != FriendshipStatus.PENDING) {
            throw ErrorCode.FRIENDSHIP_NOT_PENDING.toException()
        }

        // 요청 삭제
        friendshipRepository.delete(friendship)
    }

    /**
     * 친구 차단
     */
    @Transactional
    fun blockFriend(userId: Long, friendId: Long): FriendshipResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ErrorCode.USER_NOT_FOUND.toException() }
        val friend = userRepository.findById(friendId)
            .orElseThrow { ErrorCode.USER_NOT_FOUND.toException() }

        // 기존 친구 관계 확인
        val existingFriendships = friendshipRepository.findFriendshipBetweenUsers(userId, friendId)

        // 기존 관계가 있으면 삭제
        existingFriendships.forEach { friendshipRepository.delete(it) }

        // 차단 관계 생성
        val blockFriendship = Friendship(
            user = user,
            friend = friend,
            status = FriendshipStatus.BLOCKED
        )

        val savedFriendship = friendshipRepository.save(blockFriendship)

        return FriendShipMapper.toFriendshipResponse(savedFriendship)
    }

    /**
     * 친구 삭제 (친구 관계 끊기)
     */
    @Transactional
    fun removeFriend(userId: Long, friendId: Long) {
        val friendships = friendshipRepository.findFriendshipBetweenUsers(userId, friendId)

        if (friendships.isEmpty()) {
            throw ErrorCode.FRIENDSHIP_NOT_FOUND.toException()
        }

        // 양방향 친구 관계 모두 삭제
        friendships.forEach { friendship ->
            if (friendship.user.id == userId || friendship.friend.id == userId) {
                friendshipRepository.delete(friendship)
            }
        }
    }

    /**
     * 내 친구 목록 조회
     */
    @Transactional(readOnly = true)
    fun getMyFriends(userId: Long): FriendListResponse {
        val friendships =
            friendshipRepository.findByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED)

        val friends = FriendShipMapper.toFriendResponseList(friendships)
        return FriendShipMapper.toFriendListResponse(friends)
    }

    /**
     * 받은 친구 요청 목록 조회
     */
    @Transactional(readOnly = true)
    fun getReceivedFriendRequests(userId: Long): List<FriendshipResponse> {
        val friendships = friendshipRepository.findPendingRequestsReceivedByUser(userId)
        return FriendShipMapper.toReceivedFriendshipResponseList(friendships)
    }

    /**
     * 보낸 친구 요청 목록 조회
     */
    @Transactional(readOnly = true)
    fun getSentFriendRequests(userId: Long): List<FriendshipResponse> {
        val friendships = friendshipRepository.findPendingRequestsSentByUser(userId)
        return FriendShipMapper.toFriendshipResponseList(friendships)
    }

    /**
     * 차단 목록 조회
     */
    @Transactional(readOnly = true)
    fun getBlockedUsers(userId: Long): List<FriendResponse> {
        val friendships =
            friendshipRepository.findByUserIdAndStatus(userId, FriendshipStatus.BLOCKED)
        return FriendShipMapper.toFriendResponseList(friendships)
    }
}