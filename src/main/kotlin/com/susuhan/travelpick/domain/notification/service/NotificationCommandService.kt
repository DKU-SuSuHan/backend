package com.susuhan.travelpick.domain.notification.service

import com.susuhan.travelpick.domain.notification.dto.GroupMessageInfoResponse
import com.susuhan.travelpick.domain.notification.entity.Notification
import com.susuhan.travelpick.domain.notification.repository.NotificationRepository
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateNotFoundException
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
    private val travelRepository: TravelRepository,
    private val travelMateRepository: TravelMateRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun createNotification(
        userId: Long,
        travelId: Long,
        travelAction: TravelAction,
        travelPlace: TravelPlace? = null,
        travelMate: TravelMate? = null
    ) {
        val sendUser = userRepository.findNotDeletedUser(userId)
            ?: throw UserIdNotFoundException()

        val notificationList = travelMateRepository.findAllUserId(travelId)
            .map { mateUserId -> Notification(
                sendUser = sendUser,
                travelId = travelId,
                travelPlace = travelPlace,
                travelMate = travelMate,
                receiveUserId = mateUserId,
                travelAction = travelAction
            ) }

        notificationRepository.saveAll(notificationList)
    }

    @Transactional
    fun getGroupMessageList(userId: Long, travelId: Long): List<GroupMessageInfoResponse> {
        if (!travelRepository.existsNotDeletedPlannedTravel(travelId)) {
            throw TravelIdNotFoundException()
        }

        if (!travelMateRepository.existsNotDeletedMate(userId, travelId)) {
            throw TravelMateNotFoundException(userId)
        }

        return notificationRepository.findNotifications(userId, travelId)
            .map { GroupMessageInfoResponse.from(it) }
    }
}