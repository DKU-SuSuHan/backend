package com.susuhan.travelpick.global.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secretKey: String,
    val accessTokenExpiredTime: Long,
    val refreshTokenExpiredTime: Long,
)
