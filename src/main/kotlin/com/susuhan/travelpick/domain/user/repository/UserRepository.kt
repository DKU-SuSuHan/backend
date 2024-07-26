package com.susuhan.travelpick.domain.user.repository

import com.susuhan.travelpick.domain.user.entity.User
import org.springframework.data.repository.Repository

interface UserRepository : Repository<User, Long>, UserRepositoryQCustom {

    fun save(entity: User): User
}
