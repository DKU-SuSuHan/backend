package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelCreatorRequiredException
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.dto.request.TravelMateCreateRequest
import com.susuhan.travelpick.domain.travelmate.dto.response.TravelMateCreateResponse
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
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
        val travel = travelRepository.findById(travelId) ?: throw TravelIdNotFoundException()

        if (userId != travel.createdBy) {
            throw TravelCreatorRequiredException()
        }

        val travelMate = TravelMate(
            user = userRepository.findById(request.userId!!) ?: throw UserIdNotFoundException(),
            travel = travel,
            groupRole = GroupRole.PARTICIPANT
        )
        val savedTravelMate = travelMateRepository.save(travelMate)
        return TravelMateCreateResponse.from(savedTravelMate)
    }
}