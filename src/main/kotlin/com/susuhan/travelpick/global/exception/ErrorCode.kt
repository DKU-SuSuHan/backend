package com.susuhan.travelpick.global.exception

import org.springframework.http.HttpStatus

/**
 * 직접 정의한 에러 목록
 *
 * <ul>
 *     <li>10XX: 일반적인 서버 에러. 아래에 정의하지 않는 에러가 해당됨</li>
 *     <li>11XX: validation 관련 에러</li>
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
    CONSTRAINT_VIOLATION(1101, "데이터 유효성 검사에 실패했습니다.", HttpStatus.BAD_REQUEST)
}