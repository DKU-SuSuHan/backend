package com.susuhan.travelpick.domain.user.dto.response

import com.susuhan.travelpick.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

class LoginUserInfoResponse(
    @Schema(description = "회원의 PK")
    val id: Long?,

    @Schema(description = "회원의 닉네임")
    val nickname: String?,

    @Schema(description = "회원의 프로필 사진 S3 url 경로")
    val profileImageUrl: String?
) {

    companion object {
        fun from(user: User) = LoginUserInfoResponse(
            user.id,
            user.nickname,
            user.profileImageUrl
        )
    }
}