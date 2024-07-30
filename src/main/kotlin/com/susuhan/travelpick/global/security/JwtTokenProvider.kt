package com.susuhan.travelpick.global.security

import com.susuhan.travelpick.domain.user.constant.Role
import com.susuhan.travelpick.global.security.exception.TokenExpiredException
import com.susuhan.travelpick.global.security.exception.TokenNotValidException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.access-token-expired-time}")
    private val accessTokenExpiredTime: Long,
    @Value("\${jwt.refresh-token-expired-time}")
    private val refreshTokenExpiredTime: Long,
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
) {

    private lateinit var encodeKey: SecretKey

    @PostConstruct
    private fun init() {
        encodeKey = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
    }

    fun createAccessToken(userId: Long, role: Role): String = createToken(userId, role, accessTokenExpiredTime)

    fun createRefreshToken(userId: Long, role: Role): String = createToken(userId, role, refreshTokenExpiredTime)

    fun getAuthentication(token: String): Authentication {
        val userDetails = CustomUserDetails(getUserId(token), getUserRole(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun validateToken(token: String) {
        try {
            Jwts.parser()
                .verifyWith(encodeKey)
                .build()
                .parseSignedClaims(token)
        } catch (ex: ExpiredJwtException) {
            throw TokenExpiredException()
        } catch (ex: Exception) {
            throw TokenNotValidException()
        }
    }

    fun getUserId(token: String): String = getClaims(token).subject

    fun getUserRole(token: String) = getClaims(token)["role"].toString()

    private fun createToken(userId: Long, role: Role, tokenExpiredTime: Long): String {
        val now = Date()
        return Jwts.builder()
            .subject(userId.toString())
            .claim("role", role.name)
            .signWith(encodeKey, Jwts.SIG.HS256)
            .issuedAt(now)
            .expiration(Date(now.time + tokenExpiredTime))
            .compact()
    }

    private fun getClaims(token: String): Claims = Jwts.parser()
        .verifyWith(encodeKey)
        .build()
        .parseSignedClaims(token)
        .payload
}
