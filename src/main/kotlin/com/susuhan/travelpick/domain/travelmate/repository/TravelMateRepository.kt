package com.susuhan.travelpick.domain.travelmate.repository

import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import org.springframework.data.repository.Repository

interface TravelMateRepository : Repository<TravelMate, Long>, TravelMateRepositoryQCustom {

    fun save(entity: TravelMate): TravelMate

    fun saveAll(entities: Iterable<TravelMate>): List<TravelMate>
}