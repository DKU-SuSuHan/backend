package com.susuhan.travelpick.domain.travelplace.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.travelplace.entity.QTravelPlace.*
import org.springframework.stereotype.Repository

@Repository
class TravelPlaceRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
): TravelPlaceRepositoryQCustom {

    override fun countPlaceTotalNum(travelId: Long, travelDay: Int): Long {
        return jpaQueryFactory
            .select(travelPlace.count())
            .from(travelPlace)
            .where(
                travelPlace.travel.id.eq(travelId),
                travelPlace.travelDay.eq(travelDay)
            )
            .fetchOne() ?: 0
    }
}