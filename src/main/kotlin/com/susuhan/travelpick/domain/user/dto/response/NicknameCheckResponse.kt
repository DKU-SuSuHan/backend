package com.susuhan.travelpick.domain.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class NicknameCheckResponse (
    @Schema(description = "DB에 닉네임 존재 여부")
    val isDuplicated: Boolean,
)