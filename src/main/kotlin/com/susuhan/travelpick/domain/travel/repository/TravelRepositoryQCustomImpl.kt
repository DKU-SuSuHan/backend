package com.susuhan.travelpick.domain.travel.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.travel.entity.QTravel.travel
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.entity.constant.Status
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import org.springframework.stereotype.Repository

@Repository
class TravelRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : TravelRepositoryQCustom {

    override fun existsNotDeletedPlannedTravel(id: Long): Boolean {
        return jpaQueryFactory
            .selectOne()
            .from(travel)
            .where(
                travel.id.eq(id),
                travel.status.eq(Status.PLANNED),
                travel.deleteAt.isNull,
            )
            .fetchFirst() != null
    }

    override fun findNotDeletedPlannedTravel(id: Long): Travel? {
        return jpaQueryFactory
            .select(travel)
            .from(travel)
            .where(
                travel.id.eq(id),
                travel.status.eq(Status.PLANNED),
                travel.deleteAt.isNull,
            )
            .fetchOne()
    }

    override fun findLeaderId(id: Long): Long {
        return jpaQueryFactory
            .select(travel.leaderId)
            .from(travel)
            .where(
                travel.id.eq(id),
                travel.status.eq(Status.PLANNED),
                travel.deleteAt.isNull,
            )
            .fetchOne() ?: throw TravelIdNotFoundException()
    }
}
