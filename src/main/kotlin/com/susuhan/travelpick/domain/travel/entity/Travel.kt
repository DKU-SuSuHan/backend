package com.susuhan.travelpick.domain.travel.entity

import com.susuhan.travelpick.domain.travel.entity.constant.Status
import com.susuhan.travelpick.domain.travel.entity.constant.Theme
import com.susuhan.travelpick.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import java.time.LocalDateTime

@DynamicUpdate
@Table(name = "travels")
@Entity
class Travel(
    theme: Theme,
    title: String,
    startAt: LocalDate,
    endAt: LocalDate,
    address: Address,
    templateNum: Long,
    leaderId: Long
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id", nullable = false)
    val id: Long? = null

    @Column(name = "theme", nullable = false)
    @Enumerated(EnumType.STRING)
    var theme: Theme = theme
        protected set

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "start_at", nullable = false)
    var startAt: LocalDate = startAt
        protected set

    @Column(name = "end_at", nullable = false)
    var endAt: LocalDate = endAt
        protected set

    @Column(name = "address", nullable = false)
    @Embedded
    var address: Address = address
        protected set

    @Column(name = "template_num", nullable = false)
    var templateNum: Long = templateNum
        protected set

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: Status = Status.PLANNED
        protected set

    @Column(name = "leader_id", nullable = false)
    var leaderId: Long = leaderId
        protected set

    @Column(name = "is_private", nullable = false)
    var isPrivate: Boolean = false
        protected set

    @Column(name = "delete_at")
    var deleteAt: LocalDateTime? = null
        protected set

    /**
     * 편의 메서드 시작
     */
    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateTheme(theme: Theme) {
        this.theme = theme
    }

    fun updateStartAt(startAt: LocalDate) {
        this.startAt = startAt
    }

    fun updateEndAt(endAt: LocalDate) {
        this.endAt = endAt
    }

    fun updateAddress(address: Address) {
        this.address = address
    }

    fun updateTemplateNum(templateNum: Long) {
        this.templateNum = templateNum
    }

    fun updateLeaderId(leaderId: Long) {
        this.leaderId = leaderId
    }

    fun softDelete() {
        this.deleteAt = LocalDateTime.now()
    }
}