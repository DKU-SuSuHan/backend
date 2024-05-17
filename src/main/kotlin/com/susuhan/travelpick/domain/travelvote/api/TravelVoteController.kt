package com.susuhan.travelpick.domain.travelvote.api

import com.susuhan.travelpick.domain.travelvote.dto.request.VoteCreateRequest
import com.susuhan.travelpick.domain.travelvote.dto.response.VoteCreateResponse
import com.susuhan.travelpick.domain.travelvote.service.TravelVoteCommandService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/travels/{travelId}/votes")
@RestController
@Tag(name = "투표 관련 API")
class TravelVoteController(
    private val travelVoteCommandService: TravelVoteCommandService
){

    @Operation(
        summary = "새로운 투표 생성",
        description = "생성할 투표에 대한 정보를 받아 새 투표를 생성합니다."
        //security
    )
    @PostMapping
    fun createVote(
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody voteCreateRequest: VoteCreateRequest,
    ):ResponseEntity<VoteCreateResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(travelVoteCommandService.createVote(travelId, voteCreateRequest
            ))
    }
}