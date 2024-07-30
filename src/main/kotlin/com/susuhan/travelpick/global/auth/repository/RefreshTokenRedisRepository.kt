package com.susuhan.travelpick.global.auth.repository

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    @Value("\${jwt.refresh-token-expired-time}")
    private val refreshTokenExpiredTime: Long,
) {

    companion object {
        private const val KEY_PREFIX = "refresh_token"
    }

    fun save(userId: Long, refreshToken: String) = redisTemplate.opsForValue().set(
        "${KEY_PREFIX}:$userId",
        refreshToken,
        refreshTokenExpiredTime,
        TimeUnit.MILLISECONDS,
    )

    fun findRefreshToken(userId: String): String? = redisTemplate.opsForValue().get("${KEY_PREFIX}:$userId")
}
