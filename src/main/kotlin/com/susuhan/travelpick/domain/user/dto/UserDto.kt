package com.susuhan.travelpick.domain.user.dto

import com.susuhan.travelpick.domain.user.constant.LoginType
import com.susuhan.travelpick.domain.user.constant.Role
import com.susuhan.travelpick.domain.user.entity.User

data class UserDto(
    val id: Long? = null,
    val email: String? = null,
    val password: String? = null,
    val nickname: String,
    val socialId: String? = null,
    val profileImageUrl: String? = null,
    val role: Role? = null,
    val loginType: LoginType,
    val refreshToken: String? = null,
) {

    companion object {
        fun from(user: User): UserDto {
            return UserDto(
                user?.id,
                user?.email,
                user?.password,
                user.nickname,
                user?.socialId,
                user.profileImageUrl,
                user.role,
                user.loginType,
            )
        }
    }
}
