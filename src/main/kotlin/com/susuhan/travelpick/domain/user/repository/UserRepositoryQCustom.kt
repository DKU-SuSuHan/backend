package com.susuhan.travelpick.domain.user.repository

import com.susuhan.travelpick.domain.user.entity.User

interface UserRepositoryQCustom {
    
    fun findByNickname(nickname: String): User?
}