package com.mobileapp.drinkflow.domain.user.entity

import jakarta.persistence.*

@Entity
class Friendship(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    val friend: User,

    @Enumerated(EnumType.STRING)
    var status: FriendshipStatus = FriendshipStatus.PENDING
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

enum class FriendshipStatus {
    PENDING, ACCEPTED, BLOCKED
}
