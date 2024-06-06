package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.dto.request.LeaderDelegateRequest
import com.susuhan.travelpick.domain.travelmate.dto.request.TravelMateCreateRequest
import com.susuhan.travelpick.domain.travelmate.dto.response.LeaderDelegateResponse
import com.susuhan.travelpick.domain.travelmate.dto.response.TravelMateCreateResponse
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import com.susuhan.travelpick.global.event.TravelAction
import com.susuhan.travelpick.global.event.TravelEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TravelMateCommandService(
    private val travelMateRepository: TravelMateRepository,
    private val travelRepository: TravelRepository,
    private val userRepository: UserRepository,
    private val eventListener: ApplicationEventPublisher
) {

    @Transactional
    fun createTravelLeader(userId: Long, travel: Travel) {
        val user = userRepository.findNotDeletedUser(userId)
            ?: throw UserIdNotFoundException()

        val travelMate = TravelMate(
            user = user,
            travel = travel,
            groupRole = GroupRole.LEADER
        )

        travelMateRepository.save(travelMate)
    }

    @Transactional
    fun createTravelParticipants(
        userId: Long, travelId: Long, request: TravelMateCreateRequest
    ): List<TravelMateCreateResponse> {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, travel.leaderId)

        val travelMateList = userRepository.findAllNotDeletedUserById(request.userIds)
            .filter { user -> !travelMateRepository.existsNotDeletedMate(user.id!!, travelId) }
            .map { user -> request.toEntity(user, travel) }

        return travelMateRepository.saveAll(travelMateList)
            .map { travelMate ->
                // 그룹 알림방의 자동 메시지 생성 이벤트 발행
                eventListener.publishEvent(TravelEvent(
                    TravelAction.ADD_MATE, userId, travelId, travelMate
                ))
                TravelMateCreateResponse.from(travelMate)
            }
    }

    @Transactional
    fun softDelete(userId: Long, travelId: Long, travelMateId: Long) {
        val leaderId = travelRepository.findLeaderId(travelId)

        TravelPolicy.isTravelLeader(userId, leaderId)

        val travelMate = travelMateRepository.findNotDeletedMate(travelMateId)
            ?: throw TravelMateIdNotFoundException()

        TravelPolicy.isTravelLeaderDeletion(travelMate)

        travelMate.softDelete()

        // 그룹 알림방의 자동 메시지 생성 이벤트 발행
        eventListener.publishEvent(TravelEvent(
            TravelAction.DELETE_MATE, userId, travelId, travelMate
        ))
    }

    @Transactional
    fun softDeleteAll(travelId: Long) {
        travelMateRepository.softDeleteAll(travelId)
    }

    @Transactional
    fun delegateLeaderRole(
        userId: Long, travelId: Long, request: LeaderDelegateRequest
    ): LeaderDelegateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, travel.leaderId)

        updateTravelMateRole(userId, request.travelMateId, travel)

        return LeaderDelegateResponse.of(travelId)
    }

    private fun updateTravelMateRole(
        userId: Long, travelMateId: Long, travel: Travel
    ) {
        val leader = travelMateRepository.findNotDeletedMateByUser(travel.id!!, userId)
            ?: throw TravelMateIdNotFoundException()

        val participant = travelMateRepository.findNotDeletedMate(travelMateId)
            ?: throw TravelMateIdNotFoundException()

        travel.updateLeaderId(travelMateId)

        leader.updateToParticipantRole()
        participant.updateToLeaderRole()

        // 그룹 알림방의 자동 메시지 생성 이벤트 발행
        eventListener.publishEvent(TravelEvent(
            TravelAction.CHANGE_LEADER, userId, travel.id!!, participant
        ))
    }
}