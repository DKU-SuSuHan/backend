package com.susuhan.travelpick.domain.travel.entity

import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    var sido: String, // 행정 구역의 시, 도
    var sgg: String // 행정 구역의 시, 군, 구
)
