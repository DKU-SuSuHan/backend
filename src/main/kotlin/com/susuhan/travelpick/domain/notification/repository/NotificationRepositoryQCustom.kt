package com.susuhan.travelpick.domain.notification.repository

import com.susuhan.travelpick.domain.notification.entity.Notification

interface NotificationRepositoryQCustom {

    fun findNotifications(userId: Long, travelId: Long): List<Notification>
}