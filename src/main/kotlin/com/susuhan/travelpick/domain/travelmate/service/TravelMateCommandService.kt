package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.dto.request.TravelMateCreateRequest
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
        val user = userRepository.findById(userId) ?: throw UserIdNotFoundException()
        val travelMate = TravelMate(
            user = user,
            travel = travel,
            groupRole = GroupRole.LEADER
        )

        travelMateRepository.save(travelMate)
    }

    @Transactional
    fun createTravelParticipant(
        userId: String, travelId: Long, request: TravelMateCreateRequest
    ): TravelMateCreateResponse {
        val travel = travelRepository.findByIdAndDeleteAtIsNull(travelId) ?: throw TravelIdNotFoundException()
        TravelPolicy.isTravelCreator(userId, travel)

        val user = userRepository.findById(request.userId!!) ?: throw UserIdNotFoundException()
        val savedTravelMate = travelMateRepository.save(
            request.toEntity(user, travel)
        )

        return TravelMateCreateResponse.from(savedTravelMate)
    }

    @Transactional
    fun deleteTravelMate(userId: String, travelId: Long, travelMateId: Long) {
        val travel = travelRepository.findByIdAndDeleteAtIsNull(travelId) ?: throw TravelIdNotFoundException()
        TravelPolicy.isTravelCreator(userId, travel)

        val travelMate = travelMateRepository.findById(travelMateId) ?: throw TravelMateIdNotFoundException()
        TravelPolicy.isTravelLeaderDeletion(travelMate)

        travelMateRepository.delete(travelMate)
    }

    @Transactional
    fun deleteAll(travelId: Long) {
        travelMateRepository.deleteAllByTravelId(travelId)
    }
}