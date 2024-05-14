package com.susuhan.travelpick.domain.travelmate.entity

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "travel_mates")
@Entity
class TravelMate(
    user: User,
    travel: Travel,
    groupRole: GroupRole
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_mate_id", nullable = false)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    val travel: Travel = travel

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val  user: User = user

    @Column(name = "group_role", nullable = false)
    @Enumerated(EnumType.STRING)
    var groupRole: GroupRole = groupRole
        protected set

    @Column(name = "delete_at")
    var deleteAt: LocalDateTime? = null
        protected set

    fun updateToLeaderRole() {
        this.groupRole = GroupRole.LEADER
    }

    fun updateToParticipantRole() {
        this.groupRole = GroupRole.PARTICIPANT
    }

    fun softDelete() {
        this.deleteAt = LocalDateTime.now()
    }
}