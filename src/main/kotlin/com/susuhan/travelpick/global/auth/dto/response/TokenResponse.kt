package com.susuhan.travelpick.global.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class TokenResponse(
    @Schema(description = "서버에서 내부에서 발급한 액세스 토큰")
    val accessToken: String,

    @Schema(description = "서버에서 내부에서 발급한 리프레시 토큰")
    val refreshToken: String
) {

    companion object {
        fun of(accessToken: String, refreshToken: String) = TokenResponse(accessToken, refreshToken)
    }
}