package com.susuhan.travelpick.domain.user.api

import com.susuhan.travelpick.domain.user.dto.request.NicknameUpdateRequest
import com.susuhan.travelpick.domain.user.dto.response.NicknameUpdateResponse
import com.susuhan.travelpick.domain.user.service.UserCommandService
import com.susuhan.travelpick.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/users")
@RestController
@Tag(name = "회원 관련 API")
class UserController(
    private val userCommandService: UserCommandService
) {

    @PatchMapping
    fun updateNickname(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @Valid @RequestBody nicknameUpdateRequest: NicknameUpdateRequest
    ): ResponseEntity<NicknameUpdateResponse> {
        val userId = customUserDetails.userId.toLong()

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userCommandService.updateNickname(userId, nicknameUpdateRequest))
    }
}