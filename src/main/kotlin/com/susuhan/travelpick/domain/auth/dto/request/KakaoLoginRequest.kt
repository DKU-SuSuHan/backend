package com.susuhan.travelpick.domain.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class KakaoLoginRequest(
    @Schema(description = "카카오 서버에서 발급받은 액세스 토큰")
    @field:NotBlank
    val kakaoAccessToken: String,
)
