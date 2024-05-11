package com.susuhan.travelpick.domain.travel.repository

import com.susuhan.travelpick.domain.travel.entity.Travel
import org.springframework.data.repository.Repository

interface TravelRepository : Repository<Travel, Long> {

    fun save(entity: Travel): Travel

    fun existsByIdAndDeleteAtIsNull(id: Long): Boolean

    fun findByIdAndDeleteAtIsNull(id: Long): Travel?
}