package com.susuhan.travelpick.global.exception.dto

data class FieldError(
    val field: String,
    val value: String,
    val reason: String
)
