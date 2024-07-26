package com.susuhan.travelpick.domain.travel.entity

import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    // 행정 구역의 시, 도
    var sido: String,
    // 행정 구역의 시, 군, 구
    var sgg: String,
)
