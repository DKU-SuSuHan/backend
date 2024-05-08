package com.susuhan.travelpick.domain.travelmate.repository

import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import org.springframework.data.repository.Repository

interface TravelMateRepository : Repository<TravelMate, Long> {

    fun save(entity: TravelMate): TravelMate

    fun findById(travelMateId: Long): TravelMate?

    fun delete(entity: TravelMate)
}