package com.susuhan.travelpick.global.security.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TokenNotValidException : BusinessException(ErrorCode.TOKEN_NOT_VALID) {

    override fun isNecessaryToLog(): Boolean {
        return true
    }
}
