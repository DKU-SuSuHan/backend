package com.susuhan.travelpick.domain.travelplace.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.travelplace.entity.QTravelPlace.*
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import org.springframework.stereotype.Repository

@Repository
class TravelPlaceRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
): TravelPlaceRepositoryQCustom {

    override fun countPlaceTotalNumber(travelId: Long, travelDay: Int): Long {
        return jpaQueryFactory
            .select(travelPlace.count())
            .from(travelPlace)
            .where(
                travelPlace.travel.id.eq(travelId),
                travelPlace.travelDay.eq(travelDay),
                travelPlace.deleteAt.isNull
            )
            .fetchOne() ?: 0
    }

    override fun findNotDeletedTravelPlace(id: Long): TravelPlace? {
        return jpaQueryFactory
            .select(travelPlace)
            .from(travelPlace)
            .where(
                travelPlace.id.eq(id),
                travelPlace.deleteAt.isNull
            )
            .fetchOne()
    }

    override fun findConfirmPlaceListForDay(travelId: Long, travelDay: Int): List<TravelPlace> {
        return jpaQueryFactory
            .select(travelPlace)
            .from(travelPlace)
            .where(
                travelPlace.travel.id.eq(travelId),
                travelPlace.travelDay.eq(travelDay),
                travelPlace.deleteAt.isNull
            )
            .fetch()
    }

    override fun findOneDayBudget(travelId: Long, travelDay: Int): Long {
        return jpaQueryFactory
            .select(travelPlace.budget.sum())
            .from(travelPlace)
            .where(
                travelPlace.travel.id.eq(travelId),
                travelPlace.travelDay.eq(travelDay),
                travelPlace.deleteAt.isNull
            )
            .fetchOne() ?: 0
    }
}