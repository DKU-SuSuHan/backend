package com.susuhan.travelpick.domain.user.dto.response

import com.susuhan.travelpick.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

data class NicknameUpdateResponse (
    @Schema(description = "회원의 프로필 사진 S3 url 경로")
    val profileImageUrl: String,
    @Schema(description = "회원의 이메일")
    val email: String?,
    @Schema(description = "회원의 닉네임")
    val nickname: String?,
) {

    companion object {
        fun from(user: User) = NicknameUpdateResponse(
            user.profileImageUrl,
            user?.email,
            user?.nickname
        )
    }
}