package com.susuhan.travelpick.global.auth.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.susuhan.travelpick.domain.user.constant.LoginType
import com.susuhan.travelpick.domain.user.dto.UserDto

/**
 * 소셜 아이디 값인 id를 제외한 필드는 현재 사용하지 않음
 * 단, 필드를 선언하지 않지 않으면 에러가 발생해 Any 타입으로 지정해 파싱하지 않고 받아오기만 함
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoUserInfo(
    val id: String,
    val connectedAt: Any?,
    val kakaoAccount: Any?,
    val properties: Any?
) {

    fun toUserDto(): UserDto {
        return UserDto(
            socialId = this.id,
            loginType = LoginType.KAKAO
        )
    }
}