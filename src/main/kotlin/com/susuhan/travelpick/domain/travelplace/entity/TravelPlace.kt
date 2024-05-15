package com.susuhan.travelpick.domain.travelplace.entity

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.global.common.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@DynamicUpdate
@Table(name = "travel_places")
@Entity
class TravelPlace(
    travel: Travel,
    travelDay: Int,
    name: String,
    postcode: String,
    address: String,
    budget: Long,
    sequence: Long,
    urlLink: String?
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_place_id", nullable = false)
    val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    val travel: Travel = travel

    @Column(name = "travel_day", nullable = false)
    var travelDay: Int = travelDay
        protected set

    @Column(name = "name", nullable = false)
    var name: String = name
        protected set

    @Column(name = "postcode", nullable = false)
    var postcode: String = postcode
        protected set

    @Column(name = "address", nullable = false)
    var address: String = address
        protected set

    @Column(name = "budget", nullable = false)
    var budget: Long = budget // 1인당 예산
        protected set

    @Column(name = "sequence", unique = true, nullable = false)
    var sequence: Long = sequence
        protected set

    @Column(name = "url_link")
    var urlLink: String? = urlLink
        protected set

    @Column(name = "delete_at")
    var deleteAt: LocalDateTime? = null
        protected set

    fun updateTravelDay(travelDay: Int) {
        this.travelDay = travelDay
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updatePostcode(postcode: String) {
        this.postcode = postcode
    }

    fun updateAddress(address: String) {
        this.address = address
    }

    fun updateBudget(budget: Long) {
        this.budget = budget
    }

    fun updateUrlLink(urlLink: String) {
        this.urlLink = urlLink
    }

    fun softDelete() {
        this.deleteAt = LocalDateTime.now()
    }
}