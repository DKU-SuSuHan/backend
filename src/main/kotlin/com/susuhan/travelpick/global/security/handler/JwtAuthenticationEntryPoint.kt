package com.susuhan.travelpick.global.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.susuhan.travelpick.global.exception.ErrorCode
import com.susuhan.travelpick.global.exception.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.jvm.Throws

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint{

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        logger.error("[Authentication_Entry_Point]: 사용자가 유효한 자격증명을 제공하지 않고 접근을 시도했습니다.");

        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.characterEncoding = "utf-8"

        val errorResponse = ErrorResponse(
            ErrorCode.UNAUTHORIZED.code,
            ErrorCode.UNAUTHORIZED.errorMessage
        )

        ObjectMapper().writeValue(response?.writer, errorResponse)
    }
}
