package com.mobileapp.drinkflow.domain.drinkRecord

import com.mobileapp.drinkflow.domain.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class DrinkRecord(
    var amount: Int,

    @Column(nullable = false)
    var date: LocalDateTime,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    fun update(amount: Int) {
        amount?.let { this.amount = it }
    }
}