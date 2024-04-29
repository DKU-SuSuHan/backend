package com.susuhan.travelpick.domain.notification.entity

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.global.common.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "notifications")
@Entity
class Notification(
    user: User,
    travel: Travel,
    content: String
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    val travel: Travel = travel

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val  user: User = user

    // TODO: 여행 투표, 정해진 장소에 대한 연관관계 추가

    @Column(name = "content", nullable = false)
    var content: String = content
        private set

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false
        private set
}