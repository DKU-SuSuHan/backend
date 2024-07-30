package com.susuhan.travelpick.global.auth.repository

import com.susuhan.travelpick.global.properties.JwtProperties
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val jwtProperties: JwtProperties,
) {

    companion object {
        private const val KEY_PREFIX = "refresh_token"
    }

    fun save(userId: Long, refreshToken: String) = redisTemplate.opsForValue().set(
        "${KEY_PREFIX}:$userId",
        refreshToken,
        jwtProperties.refreshTokenExpiredTime,
        TimeUnit.MILLISECONDS,
    )

    fun findRefreshToken(userId: String): String? = redisTemplate.opsForValue().get("${KEY_PREFIX}:$userId")
}
