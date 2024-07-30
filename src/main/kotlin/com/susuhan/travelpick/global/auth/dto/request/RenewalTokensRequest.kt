package com.susuhan.travelpick.global.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class RenewalTokensRequest(
    @Schema(description = "토큰 재발급에 사용될 리프레시 토큰")
    @field:NotBlank
    val refreshToken: String,
)
