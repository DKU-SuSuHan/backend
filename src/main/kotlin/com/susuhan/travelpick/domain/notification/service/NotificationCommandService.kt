package com.susuhan.travelpick.domain.notification.service

import com.susuhan.travelpick.domain.notification.entity.Notification
import com.susuhan.travelpick.domain.notification.repository.NotificationRepository
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import com.susuhan.travelpick.global.event.TravelAction
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class NotificationCommandService(
    private val notificationRepository: NotificationRepository,
    private val travelMateRepository: TravelMateRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun sendCreatePlaceNotification(userId: Long, travelId: Long, travelPlace: TravelPlace) {
        val sendUser = userRepository.findNotDeletedUser(userId)
            ?: throw UserIdNotFoundException()

        val notificationList = travelMateRepository.findAllUserId(travelId)
            .map { mateUserId -> Notification(
                sendUser = sendUser,
                travelId = travelId,
                travelPlace = travelPlace,
                receiveUserId = mateUserId,
                travelAction = TravelAction.CREATE_PLACE
            ) }

        notificationRepository.saveAll(notificationList)
    }

    @Transactional
    fun sendDeletePlaceNotification(userId: Long, travelId: Long, travelPlace: TravelPlace) {
        val sendUser = userRepository.findNotDeletedUser(userId)
            ?: throw UserIdNotFoundException()

        val notificationList = travelMateRepository.findAllUserId(travelId)
            .map { mateUserId -> Notification(
                sendUser = sendUser,
                travelId = travelId,
                travelPlace = travelPlace,
                receiveUserId = mateUserId,
                travelAction = TravelAction.DELETE_PLACE
            ) }

        notificationRepository.saveAll(notificationList)
    }
}