package com.susuhan.travelpick.global.event

import com.susuhan.travelpick.domain.notification.service.NotificationCommandService
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class TravelEventListener(
    private val notificationCommandService: NotificationCommandService,
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleTripEvent(event: TravelEvent) {
        when (event.action) {
            TravelAction.CREATE_PLACE -> handleCreatePlace(event.userId, event.travelId, event.travelPlace!!)
            TravelAction.DELETE_PLACE -> handleDeletePlace(event.userId, event.travelId, event.travelPlace!!)
            TravelAction.CREATE_VOTE -> TODO()
            TravelAction.CLOSE_VOTE -> TODO()
            TravelAction.ADD_MATE -> handleAddMate(event.userId, event.travelId, event.travelMate!!)
            TravelAction.DELETE_MATE -> handleDeleteMate(event.userId, event.travelId, event.travelMate!!)
            TravelAction.CHANGE_LEADER -> handleChangeLeader(event.userId, event.travelId, event.travelMate!!)
        }
    }

    private fun handleCreatePlace(userId: Long, travelId: Long, travelPlace: TravelPlace) {
        notificationCommandService.createNotification(
            userId,
            travelId,
            TravelAction.CREATE_PLACE,
            travelPlace = travelPlace,
        )
    }

    private fun handleDeletePlace(userId: Long, travelId: Long, travelPlace: TravelPlace) {
        notificationCommandService.createNotification(
            userId,
            travelId,
            TravelAction.DELETE_PLACE,
            travelPlace = travelPlace,
        )
    }

    private fun handleAddMate(userId: Long, travelId: Long, travelMate: TravelMate) {
        notificationCommandService.createNotification(
            userId,
            travelId,
            TravelAction.ADD_MATE,
            travelMate = travelMate,
        )
    }

    private fun handleDeleteMate(userId: Long, travelId: Long, travelMate: TravelMate) {
        notificationCommandService.createNotification(
            userId,
            travelId,
            TravelAction.DELETE_MATE,
            travelMate = travelMate,
        )
    }

    private fun handleChangeLeader(userId: Long, travelId: Long, travelMate: TravelMate) {
        notificationCommandService.createNotification(
            userId,
            travelId,
            TravelAction.CHANGE_LEADER,
            travelMate = travelMate,
        )
    }
}
