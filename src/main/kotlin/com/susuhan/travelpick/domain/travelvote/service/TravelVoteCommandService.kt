package com.susuhan.travelpick.domain.travelvote.service

import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelvote.dto.request.TravelVoteCreateRequest
import com.susuhan.travelpick.domain.travelvote.dto.response.TravelVoteCreateResponse
import com.susuhan.travelpick.domain.travelvote.entity.TravelVote
import com.susuhan.travelpick.domain.travelvote.repository.TravelVoteRepository
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TravelVoteCommandService(
    private val travelRepository: TravelRepository,
    private val travelVoteRepository: TravelVoteRepository,
    private val travelMateRepository: TravelMateRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun createVote(
        userId: Long, travelId: Long, request: TravelVoteCreateRequest
    ): TravelVoteCreateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        if(!travelMateRepository.existsNotDeletedMate(userId, travelId)) {
            throw TravelMateNotFoundException(userId)
        }

        val user = userRepository.findNotDeletedUser(userId)
            ?: throw UserIdNotFoundException()

        val savedVote = travelVoteRepository.save(
            request.toEntity(user, travel)
        )

        // TODO : 항목 추가

        return TravelVoteCreateResponse.from(savedVote)
    }
}