package com.susuhan.travelpick.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.susuhan.travelpick.global.exception.ErrorCode
import com.susuhan.travelpick.global.exception.dto.ErrorResponse
import com.susuhan.travelpick.global.security.exception.TokenExpiredException
import com.susuhan.travelpick.global.security.exception.TokenNotValidException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

@Component
class JwtExceptionFilter : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (ex: TokenExpiredException) {
            setErrorResponse(response, ex.errorMessage)
        } catch (ex: TokenNotValidException) {
            setErrorResponse(response, ex.errorMessage)
        }
    }

    @Throws(IOException::class)
    private fun setErrorResponse(response: HttpServletResponse, errorMessage: String) {
        logger.error("[JWT_Exception_Filter]: $errorMessage")

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"

        val errorResponse = ErrorResponse(
            ErrorCode.UNAUTHORIZED.code,
            errorMessage,
        )

        ObjectMapper().writeValue(response.writer, errorResponse)
    }
}
