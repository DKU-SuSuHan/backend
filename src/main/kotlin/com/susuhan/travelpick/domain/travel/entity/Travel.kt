package com.susuhan.travelpick.domain.travel.entity

import com.susuhan.travelpick.global.common.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDate

@Table(name = "travels")
@Entity
class Travel(
    title: String,
    startAt: LocalDate,
    endAt: LocalDate,
    address: Address,
    templateNum: Long
) : BaseTimeEntity() {

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
}