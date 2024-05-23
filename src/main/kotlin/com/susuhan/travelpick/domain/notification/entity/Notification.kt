package com.susuhan.travelpick.domain.notification.entity

import com.susuhan.travelpick.global.event.TravelAction
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "notifications")
@Entity
class Notification(
    sendUser: User,
    travelPlace: TravelPlace? = null,
    travelMate: TravelMate? = null,
    travelId: Long,
    receiveUserId: Long,
    travelAction: TravelAction
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    val id: Long? = null

    // TODO: 여행 투표에 대한 연관관계 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", nullable = false)
    val sendUser: User = sendUser

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_place_id")
    val travelPlace: TravelPlace? = travelPlace

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_mate_id")
    val travelMate: TravelMate? = travelMate

    @Column(name = "travel_id", nullable = false)
    val travelId: Long = travelId

    @Column(name = "receive_user_id", nullable = false)
    val receiveUserId: Long = receiveUserId

    @Column(name = "travel_action", nullable = false)
    @Enumerated(EnumType.STRING)
    val travelAction: TravelAction = travelAction

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false
        protected set
}