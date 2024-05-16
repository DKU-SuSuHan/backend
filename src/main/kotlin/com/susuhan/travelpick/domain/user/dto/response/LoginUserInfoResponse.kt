package com.susuhan.travelpick.domain.user.dto.response

import com.susuhan.travelpick.domain.user.entity.User

class LoginUserInfoResponse(
    val id: Long?,
    val nickname: String?,
    val profileImageUrl: String?
) {

    companion object {
        fun from(user: User) = LoginUserInfoResponse(
            user?.id,
            user?.nickname,
            user?.profileImageUrl
        )
    }
}