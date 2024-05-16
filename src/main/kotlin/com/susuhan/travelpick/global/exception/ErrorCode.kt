package com.susuhan.travelpick.global.exception

import org.springframework.http.HttpStatus

/**
 * 직접 정의한 에러 목록
 *
 * <ul>
 *     <li>10XX: 일반적인 서버 에러. 아래에 정의하지 않는 에러가 해당됨</li>
 *     <li>11XX: validation 관련 에러</li>
 *     <li>12XX: auth (인증-인가) 관련 에러</li>
 *     <li>13XX: 소셜로그인 관련 에러</li>
 *     <li>14XX: 사용자 관련 에러</li>
 *     <li>15XX: 여행지 관련 에러</li>
 *     <li>16XX: 여행지 메이트 관련 에러</li>
 *     <li>17XX: 여행지 장소 관련 에러</li>
 * </ul>
 */
enum class ErrorCode(
    val code: Int,
    val errorMessage: String,
    val httpStatus: HttpStatus
) {

    UNHANDLED(1000, "알 수 없는 서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Validation Exception
     */
    METHOD_ARGUMENT_NOT_VALID(1100, "데이터 유효성 검사에 실패했습니다.", HttpStatus.BAD_REQUEST),
    CONSTRAINT_VIOLATION(1101, "데이터 유효성 검사에 실패했습니다.", HttpStatus.BAD_REQUEST),

    /**
     * Auth Exception
     */
    UNAUTHORIZED(1200, "인증 과정에 문제가 발생해 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1201, "자원에 대한 권한이 없어 접근이 거부되었습니다.", HttpStatus.FORBIDDEN),
    TOKEN_NOT_VALID(1202, "유효하지 않는 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1203, "유효기간이 만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),

    /**
     * OAuth Exception
     */
    KAKAO_SERVER_ERROR(1300, "카카오 서버와 통신 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * User Exception
     */
    USER_ID_NOT_FOUND(1400, "해당 PK를 가진 USER 엔티티가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    /**
     * Travel Exception
     */
    TRAVEL_ID_NOT_FOUND(1500, "해당 PK를 가진 Travel 엔티티가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    TRAVEL_LEADER_REQUIRED(1501, "해당 요청은 여행의 Leader 역할을 가진 사용자만 가능합니다.", HttpStatus.FORBIDDEN),

    /**
     * TravelMate Exception
     */
    TRAVEL_MATE_ID_NOT_FOUND(1600, "해당 PK를 가진 TravelMate 엔티티가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    TRAVEL_LEADER_DELETION(1601, "Leader인 TravelMate 엔티티는 삭제가 불가능합니다.", HttpStatus.FORBIDDEN),
    TRAVEL_DATE_NOT_VALID(1602, "입력 날짜는 여행 시작 날짜와 종료 날짜 사이의 값이여야 합니다.", HttpStatus.BAD_REQUEST),
    TRAVEL_MATE_NOT_FOUND(1603, "요청을 보낸 사용자는 여행에 존재하지 않는 여행 메이트 입니다.", HttpStatus.FORBIDDEN),

    /**
     * TravelPlace Exception
     */
    TRAVEL_PLACE_ID_NOT_FOUND(1700, "해당 PK를 가진 TravelPlace 엔티티가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
}
