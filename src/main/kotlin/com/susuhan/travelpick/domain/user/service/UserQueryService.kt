package com.susuhan.travelpick.domain.user.service

import com.susuhan.travelpick.domain.user.dto.response.LoginUserInfoResponse
import com.susuhan.travelpick.domain.user.dto.response.NicknameCheckResponse
import com.susuhan.travelpick.domain.user.dto.response.UserSearchByNicknameResponse
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserQueryService(
    private val userRepository: UserRepository,
) {

    fun checkNicknameDuplicated(nickname: String): NicknameCheckResponse {
        val isDuplicated = userRepository.existsNotDeletedUserByNickname(nickname)
        return NicknameCheckResponse.of(isDuplicated)
    }

    fun searchByNickname(nickname: String): UserSearchByNicknameResponse {
        val user = userRepository.findNotDeletedUserByNickname(nickname)
        return UserSearchByNicknameResponse.from(user)
    }

    fun getLoginUserInfo(id: Long): LoginUserInfoResponse {
        val user = userRepository.findNotDeletedUser(id)
            ?: throw UserIdNotFoundException()

        return LoginUserInfoResponse.from(user)
    }
}
