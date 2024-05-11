package com.susuhan.travelpick.domain.travelmate.repository

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface TravelMateRepository : Repository<TravelMate, Long> {

    fun save(entity: TravelMate): TravelMate

    fun findById(travelMateId: Long): TravelMate?

    fun delete(entity: TravelMate)

    @Modifying
    @Query("DELETE FROM TravelMate tm WHERE tm.travel.id = :travelId")
    fun deleteAllByTravelId(travelId: Long)

    // TODO: QueryDsl 사용하도록
    @Query("SELECT tm.travel FROM TravelMate tm WHERE tm.user.id = :userId AND tm.travel.startAt > CURRENT_DATE")
    fun findPlannedTravel(userId: Long): List<Travel>

    @Query("SELECT tm.travel FROM TravelMate tm WHERE tm.user.id = :userId AND tm.travel.startAt < CURRENT_DATE")
    fun findEndedTravel(userId: Long): List<Travel>
}