package com.susuhan.travelpick.global.auth.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.susuhan.travelpick.domain.user.constant.LoginType
import com.susuhan.travelpick.domain.user.entity.User

/**
 * 소셜 아이디 값인 id를 제외한 필드는 현재 사용하지 않음
 * 단, 필드를 선언하지 않지 않으면 에러가 발생해 Any 타입으로 지정해 파싱하지 않고 받아오기만 함
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoUserInfo(
    val id: String,
    val connectedAt: Any?,
    val kakaoAccount: Any?,
    val properties: Any?,
) {

    fun toEntity(): User {
        return User(
            nickname = generateRandomNickname(),
            socialId = this.id,
            loginType = LoginType.KAKAO,
        )
    }

    /**
     * 카카오 소셜 로그인의 닉네임은 랜덤한 영어 대소문자와 숫자 5개씩 조합하여 생성
     * 이 후, 사용자가 닉네임을 수정할 수 있음
     */
    private fun generateRandomNickname(): String {
        val letters = ('a'..'z') + ('A'..'Z')
        val numbers = ('0'..'9')
        return (List(5) { letters.random() } + List(5) { numbers.random() })
            .joinToString("")
    }
}
