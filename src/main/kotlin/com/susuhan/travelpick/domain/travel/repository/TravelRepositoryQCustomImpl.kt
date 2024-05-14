package com.susuhan.travelpick.domain.travel.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.travel.entity.QTravel.travel
import com.susuhan.travelpick.domain.travel.entity.Travel
import org.springframework.stereotype.Repository

@Repository
class TravelRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
): TravelRepositoryQCustom {

    override fun existNotDeletedTravel(id: Long): Boolean {
        return jpaQueryFactory
            .selectOne()
            .from(travel)
            .where(
                travel.id.eq(id),
                travel.deleteAt.isNull
            )
            .fetchFirst() != null
    }

    override fun findNotDeletedTravel(id: Long): Travel? {
        return jpaQueryFactory
            .select(travel)
            .from(travel)
            .where(
                travel.id.eq(id),
                travel.deleteAt.isNull
            )
            .fetchOne()
    }
}