package com.susuhan.travelpick.global.event

import com.susuhan.travelpick.domain.notification.service.NotificationCommandService
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class TravelEventListener(
    private val notificationCommandService: NotificationCommandService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleTripEvent(event: TravelEvent) {
        when (event.action) {
            TravelAction.CREATE_PLACE -> handleCreatePlace(event.userId, event.travelId, event.travelPlace!!)
            TravelAction.DELETE_PLACE -> handleDeletePlace(event.userId, event.travelId, event.travelPlace!!)
            TravelAction.CREATE_VOTE -> TODO()
            TravelAction.CLOSE_VOTE -> TODO()
            TravelAction.ADD_MATE -> TODO()
            TravelAction.DELETE_MATE -> TODO()
            TravelAction.CHANGE_LEADER -> TODO()
        }
    }

    private fun handleCreatePlace(userId: Long, travelId: Long, travelPlace: TravelPlace) {
        notificationCommandService.sendCreatePlaceNotification(userId, travelId, travelPlace)
    }

    private fun handleDeletePlace(userId: Long, travelId: Long, travelPlace: TravelPlace) {
        notificationCommandService.sendDeletePlaceNotification(userId, travelId, travelPlace)
    }
}