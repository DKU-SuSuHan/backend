package com.susuhan.travelpick.global.security.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TokenExpiredException : BusinessException(ErrorCode.TOKEN_EXPIRED) {

    override fun isNecessaryToLog(): Boolean {
        return true
    }
}
