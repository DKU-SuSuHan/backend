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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TravelMateCommandService(
    private val travelMateRepository: TravelMateRepository,
    private val travelRepository: TravelRepository,
    private val userRepository: UserRepository
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
            .map { user -> request.toEntity(user, travel) }

        return travelMateRepository.saveAll(travelMateList)
            .map { TravelMateCreateResponse.from(it) }
    }

    @Transactional
    fun softDelete(userId: Long, travelId: Long, travelMateId: Long) {
        val leaderId = travelRepository.findLeaderId(travelId)

        TravelPolicy.isTravelLeader(userId, leaderId)

        val travelMate = travelMateRepository.findNotDeletedMate(travelMateId)
            ?: throw TravelMateIdNotFoundException()

        TravelPolicy.isTravelLeaderDeletion(travelMate)

        travelMate.softDelete()
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

        val leader = travelMateRepository.findNotDeletedMateByUser(userId)
            ?: throw TravelMateIdNotFoundException()

        val participant = travelMateRepository.findNotDeletedMate(request.travelMateId)
            ?: throw TravelMateIdNotFoundException()

        travel.updateLeaderId(request.travelMateId)

        leader.updateToParticipantRole()
        participant.updateToLeaderRole()

        return LeaderDelegateResponse.of(travelId)
    }
}