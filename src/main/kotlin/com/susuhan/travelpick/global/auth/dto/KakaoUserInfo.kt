package com.susuhan.travelpick.global.auth.dto

import com.susuhan.travelpick.domain.user.constant.LoginType
import com.susuhan.travelpick.domain.user.dto.UserDto

data class KakaoUserInfo(
    val id: String // 카카오의 소셜 아이디
) {

    fun toUserDto(): UserDto {
        return UserDto(
            socialId = this.id,
            loginType = LoginType.KAKAO
        )
    }
}