package com.susuhan.travelpick.domain.travelvote.repository

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelvote.entity.Vote
import org.springframework.data.repository.Repository


interface TravelVoteRepository : Repository<Vote, Long> {

    fun save(entity: Vote): Vote

    fun findById(travelId: Long) : Travel
}