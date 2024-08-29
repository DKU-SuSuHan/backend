package com.susuhan.travelpick.global.common.response

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.susuhan.travelpick.global.common.exception.JsonProcessException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ApiResponseAdvice(
    private val objectMapper: ObjectMapper,
) : ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>) = true

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        val servletResponse = (response as ServletServerHttpResponse).servletResponse
        val httpStatus = HttpStatus.valueOf(servletResponse.status)

        if (httpStatus.is2xxSuccessful) {
            val apiResponse = ApiResponse.success(createStatus(request), body)

            return when {
                MappingJackson2HttpMessageConverter::class.java.isAssignableFrom(selectedConverterType) -> apiResponse
                StringHttpMessageConverter::class.java.isAssignableFrom(
                    selectedConverterType,
                ) -> writeJsonResponse(response, apiResponse)
                else -> apiResponse
            }
        }

        return ApiResponse.fail(servletResponse.status, body)
    }

    private fun createStatus(request: ServerHttpRequest) = when (request.method) {
        HttpMethod.POST -> HttpStatus.CREATED.value()
        HttpMethod.GET, HttpMethod.PUT, HttpMethod.PATCH -> HttpStatus.OK.value()
        else -> HttpStatus.NO_CONTENT.value()
    }

    private fun writeJsonResponse(response: ServerHttpResponse, apiResponse: ApiResponse<Any?>) = try {
        response.headers.contentType = MediaType.APPLICATION_JSON
        objectMapper.writeValueAsString(apiResponse)
    } catch (e: JsonProcessingException) {
        throw JsonProcessException()
    }
}
