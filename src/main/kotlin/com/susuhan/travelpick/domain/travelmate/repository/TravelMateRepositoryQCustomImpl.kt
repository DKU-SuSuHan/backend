package com.susuhan.travelpick.domain.travelmate.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelmate.entity.QTravelMate.travelMate
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class TravelMateRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
): TravelMateRepositoryQCustom {

    override fun findGroupRole(userId: Long, travelId: Long): GroupRole? {
        return jpaQueryFactory
            .select(travelMate.groupRole)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.id.eq(travelId)
            )
            .fetchOne()
    }

    override fun findAllParticipantMate(travelId: Long): List<TravelMate> {
        return jpaQueryFactory
            .select(travelMate)
            .from(travelMate)
            .innerJoin(travelMate.user).fetchJoin()
            .where(
                travelMate.groupRole.eq(GroupRole.PARTICIPANT),
                travelMate.travel.id.eq(travelId)
            )
            .fetch()
    }

    override fun findTravel(userId: Long, travelId: Long): Travel? {
        return jpaQueryFactory
            .select(travelMate.travel)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.id.eq(travelId)
            )
            .fetchOne()
    }

    override fun findPlannedTravel(userId: Long): List<Travel> {
        return jpaQueryFactory
            .select(travelMate.travel)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.startAt.goe(LocalDate.now())
            )
            .fetch()
    }

    override fun findEndedTravel(userId: Long): List<Travel> {
        return jpaQueryFactory
            .select(travelMate.travel)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.startAt.lt(LocalDate.now())
            )
            .fetch()
    }

    override fun deleteAll(travelId: Long) {
        jpaQueryFactory
            .delete(travelMate)
            .where(travelMate.travel.id.eq(travelId))
            .execute()
    }
}