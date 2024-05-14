package com.susuhan.travelpick.domain.travel.repository

import com.susuhan.travelpick.domain.travel.entity.Travel

interface TravelRepositoryQCustom {

    fun existNotDeletedTravel(id: Long): Boolean

    fun findNotDeletedTravel(id: Long): Travel?
}