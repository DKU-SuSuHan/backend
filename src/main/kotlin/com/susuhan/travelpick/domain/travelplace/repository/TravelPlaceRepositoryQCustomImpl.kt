package com.susuhan.travelpick.domain.travelplace.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.travelplace.dto.AddressInfo
import com.susuhan.travelpick.domain.travelplace.entity.QTravelPlace.travelPlace
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import org.springframework.stereotype.Repository

@Repository
class TravelPlaceRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : TravelPlaceRepositoryQCustom {

    override fun countPlaceTotalNumber(travelId: Long, travelDay: Int): Long {
        return jpaQueryFactory
            .select(travelPlace.count())
            .from(travelPlace)
            .where(
                travelPlace.travel.id.eq(travelId),
                travelPlace.travelDay.eq(travelDay),
                travelPlace.deleteAt.isNull,
            )
            .fetchOne() ?: 0
    }

    override fun findNotDeletedTravelPlace(id: Long): TravelPlace? {
        return jpaQueryFactory
            .select(travelPlace)
            .from(travelPlace)
            .where(
                travelPlace.id.eq(id),
                travelPlace.deleteAt.isNull,
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
                travelPlace.deleteAt.isNull,
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
                travelPlace.deleteAt.isNull,
            )
            .fetchOne() ?: 0
    }

    override fun findTotalBudget(travelId: Long): Long {
        return jpaQueryFactory
            .select(travelPlace.budget.sum())
            .from(travelPlace)
            .where(
                travelPlace.travel.id.eq(travelId),
                travelPlace.deleteAt.isNull,
            )
            .fetchOne() ?: 0
    }

    override fun findPostcodeAndAddress(travelId: Long): List<AddressInfo> {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    AddressInfo::class.java,
                    travelPlace.id,
                    travelPlace.postcode,
                    travelPlace.address,
                ),
            )
            .from(travelPlace)
            .where(
                travelPlace.travel.id.eq(travelId),
                travelPlace.deleteAt.isNull,
            )
            .fetch()
    }

    override fun incrementSequence(sequence: Long) {
        jpaQueryFactory
            .update(travelPlace)
            .set(travelPlace.sequence, travelPlace.sequence.add(1))
            .where(travelPlace.sequence.eq(sequence))
            .execute()
    }

    override fun decrementSequence(sequence: Long) {
        jpaQueryFactory
            .update(travelPlace)
            .set(travelPlace.sequence, travelPlace.sequence.subtract(1))
            .where(travelPlace.sequence.eq(sequence))
            .execute()
    }

    override fun incrementAllSequence(sequence: Long) {
        jpaQueryFactory
            .update(travelPlace)
            .set(travelPlace.sequence, travelPlace.sequence.add(1))
            .where(travelPlace.sequence.lt(sequence))
            .execute()
    }

    override fun decrementAllSequence(sequence: Long) {
        jpaQueryFactory
            .update(travelPlace)
            .set(travelPlace.sequence, travelPlace.sequence.subtract(1))
            .where(travelPlace.sequence.gt(sequence))
            .execute()
    }
}
