package com.susuhan.travelpick.domain.user.api

import com.susuhan.travelpick.domain.user.dto.request.NicknameUpdateRequest
import com.susuhan.travelpick.domain.user.dto.response.LoginUserInfoResponse
import com.susuhan.travelpick.domain.user.dto.response.NicknameCheckResponse
import com.susuhan.travelpick.domain.user.dto.response.NicknameUpdateResponse
import com.susuhan.travelpick.domain.user.dto.response.UserSearchByNicknameResponse
import com.susuhan.travelpick.domain.user.service.UserCommandService
import com.susuhan.travelpick.domain.user.service.UserQueryService
import com.susuhan.travelpick.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RequestMapping("/api/v1/users")
@RestController
@Tag(name = "회원 관련 API")
class UserController(
    private val userCommandService: UserCommandService,
    private val userQueryService: UserQueryService,
) {

    @Operation(
        summary = "회원의 닉네임 수정",
        description = "회원이 변경하고자 하는 닉네임을 전달받아 닉네임을 수정합니다.",
        security = [SecurityRequirement(name = "access-token")],
    )
    @PatchMapping("/nickname")
    fun updateNickname(
        @Parameter(hidden = true) @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @Valid @RequestBody nicknameUpdateRequest: NicknameUpdateRequest,
    ): ResponseEntity<NicknameUpdateResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                userCommandService.updateNickname(
                    customUserDetails.getUserId(),
                    nicknameUpdateRequest,
                ),
            )
    }

    @Operation(
        summary = "회원의 닉네임 중복 확인",
        description = "회원이 변경하고자 하는 닉네임을 전달받아 DB에 존재하는지 확인합니다.",
        security = [SecurityRequirement(name = "access-token")],
    )
    @GetMapping("/check/nickname")
    fun checkNicknameDuplicated(
        @NotBlank @Size(max = 10) @RequestParam nickname: String,
    ): ResponseEntity<NicknameCheckResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userQueryService.checkNicknameDuplicated(nickname))
    }

    @Operation(
        summary = "여행 메이트로 추가할 회원 정보 조회",
        description = "전달 받은 회원의 닉네임이 존재한다면 회원의 정보를 반환하고 존재하지 않으면 null로 반환합니다.",
        security = [SecurityRequirement(name = "access-token")],
    )
    @GetMapping("/search")
    fun search(@NotBlank @Size(max = 10) @RequestParam nickname: String): ResponseEntity<UserSearchByNicknameResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userQueryService.searchByNickname(nickname))
    }

    @Operation(
        summary = "로그인한 회원 정보 조회",
        description = "로그인한 회원의 정보를 조회해 반환합니다.",
        security = [SecurityRequirement(name = "access-token")],
    )
    @GetMapping("/login")
    fun getLoginUserInfo(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
    ): ResponseEntity<LoginUserInfoResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userQueryService.getLoginUserInfo(customUserDetails.getUserId()))
    }
}
