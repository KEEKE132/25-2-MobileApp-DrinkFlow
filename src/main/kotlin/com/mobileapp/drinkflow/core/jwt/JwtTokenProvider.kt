package com.mobileapp.drinkflow.core.jwt

import com.mobileapp.drinkflow.domain.user.entity.User
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.MacAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val jwtConfig: JwtConfig
) {
    private val macAlgorithm: MacAlgorithm = Jwts.SIG.HS256
    private val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    private val jwtParser: JwtParser
        get() = Jwts.parser()
            .verifyWith(secretKey)
            .build()

    fun issueTokenPair(user: User): TokenPair {
        return TokenPair(
            accessToken = issueAccessToken(user),
            refreshToken = issueRefreshToken(user)
        )
    }

    fun issueAccessToken(user: User): String {
        return issue(user, jwtConfig.expire.access)
    }

    fun issueRefreshToken(user: User): String {
        return issue(user, jwtConfig.expire.refresh)
    }

    private fun issue(user: User, expTime: Long): String {
        return Jwts.builder()
            .subject(user.id.toString())
            .claim("email", user.username)
            .issuedAt(Date())
            .expiration(Date(Date().time + expTime))
            .signWith(secretKey, macAlgorithm)
            .compact()
    }

    private val secretKey: SecretKey
        get() = Keys.hmacShaKeyFor(jwtConfig.secretKey.toByteArray())

    fun validate(token: String) {
        jwtParser.parseSignedClaims(token)
    }


    fun parseJwt(token: String): TokenBody {
        val claimsJws = jwtParser.parseSignedClaims(token)
        val payload = claimsJws.payload
        val id = payload.subject.toLong()
        val username = payload.get("user", String::class.java)

        return TokenBody(
            id = id,
            username = username
        )
    }
}