package com.susuhan.travelpick.global.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.susuhan.travelpick.global.exception.ErrorCode
import com.susuhan.travelpick.global.exception.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.jvm.Throws

@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        logger.error("[Access_Denied_Handler]: 사용자가 필요한 권한이 없는 상태로 접근했습니다.");

        response?.status = HttpStatus.FORBIDDEN.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.characterEncoding = "utf-8"

        val errorResponse = ErrorResponse(
            ErrorCode.FORBIDDEN.code,
            ErrorCode.FORBIDDEN.errorMessage
        )

        ObjectMapper().writeValue(response?.writer, errorResponse)
    }
}
