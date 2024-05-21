package com.susuhan.travelpick.domain.travel.entity

import com.susuhan.travelpick.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Table(name = "categories")
@Entity
class Category(
    name: String
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    val id: Long? = null

    @Column(name = "name", nullable = false)
    var name: String = name

}
