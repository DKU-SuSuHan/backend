package com.susuhan.travelpick.domain.travelplace.repository

import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import org.springframework.data.repository.Repository

interface TravelPlaceRepository : Repository<TravelPlace, Long>, TravelPlaceRepositoryQCustom {

    fun save(entity: TravelPlace): TravelPlace
}