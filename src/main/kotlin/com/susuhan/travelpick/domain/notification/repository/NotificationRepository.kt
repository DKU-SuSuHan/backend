package com.susuhan.travelpick.domain.notification.repository

import com.susuhan.travelpick.domain.notification.entity.Notification
import org.springframework.data.repository.Repository

interface NotificationRepository : Repository<Notification, Long>, NotificationRepositoryQCustom {

    fun saveAll(entities: Iterable<Notification>): List<Notification>
}
