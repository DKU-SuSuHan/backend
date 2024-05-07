package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.domain.travel.entity.Travel
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
}