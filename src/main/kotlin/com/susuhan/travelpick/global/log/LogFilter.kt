package com.susuhan.travelpick.global.log

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.jboss.logging.MDC
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.util.*

class LogFilter : OncePerRequestFilter() {

    companion object {
        private const val MDC_LOG_TRACED_ID_KEY = "LogTraceId"

        private val NO_LOG_URL_LIST = listOf("/swagger-ui", "/v3/api-docs")

        private val VISIBLE_TYPES: List<MediaType> = listOf(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.MULTIPART_FORM_DATA
        )
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        MDC.put(MDC_LOG_TRACED_ID_KEY, UUID.randomUUID().toString().substring(0, 12))

        try {
            if (isAsyncDispatch(request)) {
                filterChain.doFilter(request, response)
            } else {
                logRequestAndResponse(request, response, filterChain)
            }
        } finally {
            MDC.clear()
        }
    }

    /**
     * multipart/form-data로 데이터를 전달할 경우 MultipartHttpServletRequest가 데이터를 받기 때문에
     * HttpServletRequestWrapper를 상속한 RequestWrapper로 변환할 시 에러가 발생해 분기해 처리
     */
    @Throws(IOException::class, ServletException::class)
    private fun logRequestAndResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val isLog = !NO_LOG_URL_LIST.any { request.requestURI.contains(it) }
        val responseWrapper = ResponseWrapper(response)

        try {
            if (isMultipartFormData(request.contentType)) {
                logger.info("[${MDC.get(MDC_LOG_TRACED_ID_KEY)}] Request - [${request.method}]" +
                        ": uri=${request.requestURI}, payload=multipart/form-data")
                filterChain.doFilter(request, responseWrapper)
            } else {
                val requestWrapper = RequestWrapper(request)
                if (isLog) logRequest(requestWrapper)
                filterChain.doFilter(requestWrapper, responseWrapper)
            }
        } finally {
            if (isLog) logResponse(responseWrapper)
            responseWrapper.copyBodyToResponse()
        }
    }

    @Throws(IOException::class)
    private fun logRequest(request: RequestWrapper) {
        val logTraceId = MDC.get(MDC_LOG_TRACED_ID_KEY).toString()
        var uri = request.requestURI
        val queryString = request.queryString
        val content = request.inputStream.use { it.readBytes() }

        queryString?.let { uri += "?$queryString"}

        val payloadInfo = when {
            content.isEmpty() -> "NO_CONTENT"
            else -> "${getPayloadInfo(request.contentType, content)}"
        }

        logger.info("[$logTraceId] Request - [${request.method}]: uri=$uri $payloadInfo")
    }

    @Throws(IOException::class)
    private fun logResponse(response: ContentCachingResponseWrapper) {
        val logTraceId = java.lang.String.valueOf(MDC.get(MDC_LOG_TRACED_ID_KEY))
        val content = response.contentInputStream.use { it.readBytes() }
        val payloadInfo = when {
            content.isEmpty() -> "NO_CONTENT"
            else -> "${getPayloadInfo(response.contentType, content)}"
        }

        logger.info("[$logTraceId] Response - $payloadInfo")
    }

    private fun getPayloadInfo(contentType: String, content: ByteArray): String {
        val payloadInfo = "content-type=$contentType, payload="
        val payload = when {
            !isVisible(MediaType.valueOf(contentType)) -> "Binary Content"
            else -> String(content).trimIndent().replace(Regex("\\n *"), "").replace(",", ", ")
        }
        return payloadInfo + payload
    }

    private fun isMultipartFormData(contentType: String?): Boolean {
        return contentType?.contains(MediaType.MULTIPART_FORM_DATA_VALUE) ?: false
    }

    private fun isVisible(mediaType: MediaType): Boolean {
        return VISIBLE_TYPES.any { visibleType -> visibleType.includes(mediaType) }
    }
}
