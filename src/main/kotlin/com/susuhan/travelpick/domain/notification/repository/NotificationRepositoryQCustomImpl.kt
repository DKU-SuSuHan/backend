package com.susuhan.travelpick.domain.notification.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.susuhan.travelpick.domain.notification.entity.Notification
import com.susuhan.travelpick.domain.notification.entity.QNotification.notification
import com.susuhan.travelpick.domain.travel.entity.QTravel.travel
import com.susuhan.travelpick.domain.travelmate.entity.QTravelMate.travelMate
import com.susuhan.travelpick.domain.travelplace.entity.QTravelPlace.travelPlace
import com.susuhan.travelpick.domain.user.entity.QUser
import org.springframework.stereotype.Repository

@Repository
class NotificationRepositoryQCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : NotificationRepositoryQCustom {

    override fun findNotifications(userId: Long, travelId: Long): List<Notification> {
        // 그룹 메시지 목록 조회 시 isRead 필드를 true로 업데이트
        jpaQueryFactory.update(notification)
            .set(notification.isRead, true)
            .where(
                notification.receiveUserId.eq(userId),
                notification.travelId.eq(travelId),
            )
            .execute()

        return jpaQueryFactory
            .select(notification)
            .from(notification)
            .innerJoin(notification.sendUser, QUser("sendUser")).fetchJoin()
            .leftJoin(notification.travelPlace, travelPlace).fetchJoin()
            .leftJoin(notification.travelPlace.travel, travel).fetchJoin()
            .leftJoin(notification.travelMate, travelMate).fetchJoin()
            .leftJoin(notification.travelMate.user, QUser("travelMateUser")).fetchJoin()
            .where(
                notification.receiveUserId.eq(userId),
                notification.travelId.eq(travelId),
            )
            .fetch()
    }
}
