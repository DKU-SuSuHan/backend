package com.susuhan.travelpick.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema

class UserProfileInfo(
    @Schema(description = "회원의 닉네임")
    val nickname: String?,

    @Schema(description = "회원의 프로필 사진 S3 url 경로")
    val profileImageUrl: String
)