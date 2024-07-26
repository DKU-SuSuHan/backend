package com.susuhan.travelpick.global.common.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity : BaseTimeEntity() {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: String? = null
        protected set

    @LastModifiedBy
    @Column(nullable = false)
    var updatedBy: String? = null
        protected set
}
