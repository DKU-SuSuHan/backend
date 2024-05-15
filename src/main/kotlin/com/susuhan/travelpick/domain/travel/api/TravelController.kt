package com.susuhan.travelpick.domain.travel.api

import com.susuhan.travelpick.domain.travel.dto.request.TravelCreateRequest
import com.susuhan.travelpick.domain.travel.dto.request.TravelUpdateRequest
import com.susuhan.travelpick.domain.travel.dto.response.MyTravelResponse
import com.susuhan.travelpick.domain.travel.dto.response.MyTravelInfoResponse
import com.susuhan.travelpick.domain.travel.dto.response.TravelCreateResponse
import com.susuhan.travelpick.domain.travel.dto.response.TravelUpdateResponse
import com.susuhan.travelpick.domain.travel.entity.constant.Status
import com.susuhan.travelpick.domain.travel.service.TravelCommandService
import com.susuhan.travelpick.domain.travel.service.TravelQueryService
import com.susuhan.travelpick.global.common.validator.EnumValid
import com.susuhan.travelpick.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/travels")
@RestController
@Tag(name = "여행 관련 API")
class TravelController(
    private val travelCommandService: TravelCommandService,
    private val travelQueryService: TravelQueryService
) {

    @Operation(
        summary = "새 여행지 생성",
        description = "새로운 여행지에 대한 데이터를 받아 새 여행지를 생성합니다.",
        security = [SecurityRequirement(name = "access-token")]
    )
    @PostMapping
    fun create(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @Valid @RequestBody travelCreateRequest: TravelCreateRequest
    ): ResponseEntity<TravelCreateResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(travelCommandService.create(
                customUserDetails.getUserId(), travelCreateRequest
            ))
    }

    @Operation(
        summary = "여행지 수정",
        description = """
            여행지에 대한 데이터를 받아 여행지의 데이터를 수정합니다. 
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @PutMapping("/{travelId}")
    fun update(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody travelUpdateRequest: TravelUpdateRequest
    ): ResponseEntity<TravelUpdateResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelCommandService.update(
                customUserDetails.getUserId(), travelId, travelUpdateRequest
            ))
    }

    @Operation(
        summary = "여행지 삭제",
        description = """
            여행지의 PK를 전달받아 해당 여행지를 삭제합니다. 
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @DeleteMapping("/{travelId}")
    fun softDelete(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(travelCommandService.softDelete(
                customUserDetails.getUserId(), travelId
            ))
    }

    @Operation(
        summary = "나의 여행지 단건 조회",
        description = "특정 여행지에 대한 데이터를 조회합니다.",
        security = [SecurityRequirement(name = "access-token")]
    )
    @GetMapping("/{travelId}")
    fun getMyTravel(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long
    ): ResponseEntity<MyTravelInfoResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelQueryService.getMyTravel(
                customUserDetails.getUserId(), travelId
            ))
    }


    @Operation(
        summary = "예정 / 종료된 나의 여행지 목록 조회",
        description = """
            로그인한 회원의 예정되거나 종료된 여행지 목록을 조회합니다.
            [1] 예정된 여행지: status=planned
            [2] 종료된 여행지: status=ended
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @GetMapping
    fun getMyTravelList(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestParam @EnumValid(enumClass = Status::class) status: String
    ): ResponseEntity<List<MyTravelResponse>> {
        val userId = customUserDetails.getUserId()

        val response = when (enumValueOf<Status>(status.uppercase())) {
            Status.PLANNED -> travelQueryService.getPlannedTravelList(userId)
            Status.ENDED -> travelQueryService.getEndedTravelList(userId)
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response)
    }
}