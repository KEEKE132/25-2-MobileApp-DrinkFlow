package com.mobileapp.drinkflow.core.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    @Value("\${custom.cors.origins}")
    private lateinit var corsOrigins: List<String>

    override fun addCorsMappings(registry: CorsRegistry) {
        val allowedOrigins = mutableListOf(
            "http://localhost:3000",
            "http://localhost:8080",
            "http://localhost:5500",
            "http://localhost:5173",
            "http://http://drinkflow.p-e.kr"
        )
        allowedOrigins.addAll(corsOrigins)

        registry.addMapping("/**")
            .allowedOrigins(*allowedOrigins.toTypedArray())
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization", "authorization")
            .allowCredentials(true)
            .maxAge(3600)
    }
}

