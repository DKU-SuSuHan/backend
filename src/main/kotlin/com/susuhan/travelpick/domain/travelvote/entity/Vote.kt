package com.susuhan.travelpick.domain.travelvote.entity

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelvote.entity.constant.Category
import jakarta.persistence.*
import java.time.LocalDate


@Table(name = "travel_votes")
@Entity
class Vote(
    travel: Travel,
    title: String,
    expiredAt: LocalDate,
    isSingle: Boolean,

    //추가 : 카테고리
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_vote_id", nullable = false)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    val travel: Travel = travel

//    @Column(name = "category", nullable = false)
//    @Enumerated(EnumType.STRING)
//    val category: Category = category

    @Column(name = "title", nullable = false)
    val title: String = title

    @Column(name = "expired_at", nullable = false)
    val expiredAt: LocalDate = expiredAt

    @Column(name = "is_single", nullable = false)
    val isSingle: Boolean = isSingle

    @Column(name = "is_confirmed", nullable = false)
    val isConfirmed: Boolean = false

    // 추가 : 투표수 - var count

}