package com.susuhan.travelpick.domain.user.repository

import com.susuhan.travelpick.domain.user.entity.User

interface UserRepositoryQCustom {

    fun existsNotDeletedUserByNickname(nickname: String): Boolean

    fun findNotDeletedUser(id: Long): User?

    fun findNotDeletedUserByNickname(nickname: String): User?

    fun findNotDeletedUserBySocialId(socialId: String): User?
}