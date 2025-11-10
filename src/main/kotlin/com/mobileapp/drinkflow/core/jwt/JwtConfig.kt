package com.mobileapp.drinkflow.core.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom.jwt")
data class JwtConfig(
    val expire: Expire,
    val secretKey: String
){
    data class Expire(
        val access: Long,
        val refresh: Long
    )
}
