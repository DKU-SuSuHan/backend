package com.susuhan.travelpick.global.exception

import org.springframework.http.HttpStatus

abstract class BusinessException : RuntimeException {

    val code: Int
    val errorMessage: String
    val httpStatus: HttpStatus

    constructor(errorCode: ErrorCode) : super() {
        this.code = errorCode.code
        this.errorMessage = errorCode.errorMessage
        this.httpStatus = errorCode.httpStatus
    }

    constructor(errorCode: ErrorCode, optionalMessage: String) : super() {
        this.code = errorCode.code
        this.errorMessage = "${errorCode.errorMessage} $optionalMessage"
        this.httpStatus = errorCode.httpStatus
    }

    abstract fun isNecessaryToLog(): Boolean
}
