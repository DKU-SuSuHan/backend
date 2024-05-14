package com.susuhan.travelpick.domain.travelmate.repository

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole

interface TravelMateRepositoryQCustom {

    fun existNotDeletedMate(userId: Long, travelId: Long): Boolean

    fun findNotDeletedMate(id: Long): TravelMate?

    fun findGroupRole(userId: Long, travelId: Long): GroupRole?

    fun findAllParticipantMate(travelId: Long): List<TravelMate>

    fun findTravel(userId: Long, travelId: Long): Travel?

    fun findPlannedTravel(userId: Long): List<Travel>

    fun findEndedTravel(userId: Long): List<Travel>

    fun softDeleteAll(travelId: Long)
}