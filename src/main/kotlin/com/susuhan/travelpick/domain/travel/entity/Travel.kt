package com.susuhan.travelpick.domain.travel.entity

import com.susuhan.travelpick.global.common.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Table(name = "travels")
@Entity
class Travel(
    title: String,
    startAt: LocalDate,
    endAt: LocalDate,
    address: Address,
    templateNum: Long
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id", nullable = false)
    val id: Long? = null

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

    @Column(name = "is_private", nullable = false)
    var isPrivate: Boolean = false
        protected set

    @Column(name = "delete_at")
    var deleteAt: LocalDateTime? = null
        protected set

    fun updateTitle(title: String) {
        this.title = title
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

    fun softDelete() {
        this.deleteAt = LocalDateTime.now()
    }
}