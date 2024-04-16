package com.susuhan.travelpick.domain.user.entity

import com.susuhan.travelpick.domain.user.constant.LoginType
import com.susuhan.travelpick.domain.user.constant.Role
import com.susuhan.travelpick.global.common.BaseTimeEntity
import com.susuhan.travelpick.global.common.ConstantUtils
import jakarta.persistence.*

@Table(name = "users")
@Entity
class User(
    email: String? = null,
    password: String? = null,
    nickname: String? = null,
    socialId: String? = null,
    loginType: LoginType
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long? = null

    @Column(name = "email", unique = true)
    var email: String? = email
        private set

    @Column(name = "password", length = 20)
    var password: String? = password
        private set

    @Column(name = "nickname", unique = true, length = 10)
    var nickname: String? = nickname
        private set

    @Column(name = "social_id", unique = true)
    val socialId: String? = socialId

    @Column(name = "profile_image_url")
    var profileImageUrl: String = ConstantUtils.DEFAULT_PROFILE_IMAGE_URL
        private set

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
        private set

    @Column(name = "login_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val loginType: LoginType = loginType

    // TODO: redis로 관리할 예정
    @Column(name = "refresh_token")
    var refreshToken: String? = null

    fun updateNickname(nickname: String) {
        this.nickname = nickname
    }

    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}