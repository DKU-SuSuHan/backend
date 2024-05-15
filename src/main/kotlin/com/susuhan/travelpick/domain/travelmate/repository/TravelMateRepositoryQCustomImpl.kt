package com.susuhan.travelpick.domain.travelmate.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelmate.entity.QTravelMate.travelMate
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class TravelMateRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
): TravelMateRepositoryQCustom {

    override fun existNotDeletedMate(userId: Long, travelId: Long): Boolean {
        return jpaQueryFactory
            .selectOne()
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.id.eq(travelId),
                travelMate.deleteAt.isNull
            )
            .fetchFirst() != null
    }

    override fun findNotDeletedMate(id: Long): TravelMate? {
        return jpaQueryFactory
            .select(travelMate)
            .from(travelMate)
            .where(
                travelMate.id.eq(id),
                travelMate.deleteAt.isNull
            )
            .fetchOne()
    }

    override fun findNotDeletedMateByUser(userId: Long): TravelMate? {
        return jpaQueryFactory
            .select(travelMate)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.deleteAt.isNull
            )
            .fetchOne()
    }

    override fun findGroupRole(userId: Long, travelId: Long): GroupRole? {
        return jpaQueryFactory
            .select(travelMate.groupRole)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.id.eq(travelId),
                travelMate.deleteAt.isNull
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
                travelMate.travel.id.eq(travelId),
                travelMate.deleteAt.isNull
            )
            .fetch()
    }

    override fun findTravel(userId: Long, travelId: Long): Travel? {
        return jpaQueryFactory
            .select(travelMate.travel)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.id.eq(travelId),
                travelMate.deleteAt.isNull
            )
            .fetchOne()
    }

    override fun findPlannedTravel(userId: Long): List<Travel> {
        return jpaQueryFactory
            .select(travelMate.travel)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.startAt.goe(LocalDate.now()),
                travelMate.deleteAt.isNull
            )
            .fetch()
    }

    override fun findEndedTravel(userId: Long): List<Travel> {
        return jpaQueryFactory
            .select(travelMate.travel)
            .from(travelMate)
            .where(
                travelMate.user.id.eq(userId),
                travelMate.travel.startAt.lt(LocalDate.now()),
                travelMate.deleteAt.isNull
            )
            .fetch()
    }

    override fun softDeleteAll(travelId: Long) {
        jpaQueryFactory
            .update(travelMate)
            .set(travelMate.deleteAt, LocalDateTime.now())
            .where(
                travelMate.travel.id.eq(travelId),
                travelMate.deleteAt.isNull
            )
            .execute()
    }
}