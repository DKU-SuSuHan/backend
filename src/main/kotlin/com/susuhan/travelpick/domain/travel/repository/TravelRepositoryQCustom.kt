package com.susuhan.travelpick.domain.travel.repository

import com.susuhan.travelpick.domain.travel.entity.Travel

interface TravelRepositoryQCustom {

    fun existNotDeletedPlannedTravel(id: Long): Boolean

    fun findNotDeletedPlannedTravel(id: Long): Travel?
}