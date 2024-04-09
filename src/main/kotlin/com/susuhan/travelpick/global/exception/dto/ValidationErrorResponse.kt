package com.susuhan.travelpick.global.exception.dto

data class ValidationErrorResponse(
    val code: Int,
    val message: String,
    val errors: List<FieldError>
)
