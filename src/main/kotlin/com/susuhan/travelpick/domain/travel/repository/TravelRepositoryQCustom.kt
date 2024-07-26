package com.susuhan.travelpick.domain.travel.repository

import com.susuhan.travelpick.domain.travel.entity.Travel

interface TravelRepositoryQCustom {

    fun existsNotDeletedPlannedTravel(id: Long): Boolean

    fun findNotDeletedPlannedTravel(id: Long): Travel?

    fun findLeaderId(id: Long): Long
}
