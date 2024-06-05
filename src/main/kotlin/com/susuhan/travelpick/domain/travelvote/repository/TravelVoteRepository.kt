package com.susuhan.travelpick.domain.travelvote.repository

import com.susuhan.travelpick.domain.travelvote.entity.TravelVote
import org.springframework.data.repository.Repository


interface TravelVoteRepository : Repository<TravelVote, Long> {

    fun save(entity: TravelVote): TravelVote
}