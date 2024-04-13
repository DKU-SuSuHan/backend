package com.susuhan.travelpick.domain.user.repository

import com.susuhan.travelpick.domain.user.entity.User
import org.springframework.data.repository.Repository

interface UserRepository : Repository<User, Long> {

    fun save(entity: User): User

    fun findById(id: Long): User?

    fun findBySocialId(id: String): User?
}