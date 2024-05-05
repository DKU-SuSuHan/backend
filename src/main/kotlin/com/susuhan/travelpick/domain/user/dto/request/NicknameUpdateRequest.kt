package com.susuhan.travelpick.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class NicknameUpdateRequest(
    @Schema(description = "수정할 회원의 닉네임")
    @field:NotBlank
    @field:Size(max = 10, message = "닉네임은 10자 이하여야 합니다.")
    val nickname: String
)