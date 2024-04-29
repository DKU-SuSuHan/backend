package com.susuhan.travelpick.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class NicknameCheckRequest(
    @Schema(description = "중복 여부를 조회할 닉네임")
    @field:NotBlank(message = "닉네임은 공백이나 빈 값이 아니여야 합니다.")
    @field:Size(max = 10, message = "닉네임은 10자 이하여야 합니다.")
    val nickname: String
)