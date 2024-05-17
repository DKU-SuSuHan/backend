package com.susuhan.travelpick.domain.travelvote.service

import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelvote.dto.request.VoteCreateRequest
import com.susuhan.travelpick.domain.travelvote.dto.response.VoteCreateResponse
import com.susuhan.travelpick.domain.travelvote.entity.Vote
import com.susuhan.travelpick.domain.travelvote.repository.TravelVoteRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TravelVoteCommandService(
    private val travelRepository: TravelRepository,
    private val travelVoteRepository: TravelVoteRepository,
) {

    @Transactional
    fun createVote(
        travelId: Long, request: VoteCreateRequest): VoteCreateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()
        val vote = Vote(
            travel = travel,
            title = request.title,
            expiredAt = request.expiredAt,
            isSingle = request.isSingle,
        )
        val savedVote = travelVoteRepository.save(vote)

        // 추가 : 항목 생성 기능

        return VoteCreateResponse.from(savedVote)
    }
}