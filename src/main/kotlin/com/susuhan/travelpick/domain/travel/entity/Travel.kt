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
        private set

    @Column(name = "start_at", nullable = false)
    var startAt: LocalDate = startAt
        private set

    @Column(name = "end_at", nullable = false)
    var endAt: LocalDate = endAt
        private set

    @Column(name = "address", nullable = false)
    @Embedded
    var address: Address = address
        private set

    @Column(name = "template_num", nullable = false)
    var templateNum: Long = templateNum
        private set

    @Column(name = "is_private", nullable = false)
    var isPrivate: Boolean = false
        private set
}