package com.mobileapp.drinkflow.core.jwt

import com.mobileapp.drinkflow.domain.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(updatable = false, nullable = false)
    var token: String? = null

    @CreatedDate
    @Column(updatable = false, nullable = false)
    var issuedAt: LocalDateTime? = null

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        unique = true
    )
    val user: User? = null
}