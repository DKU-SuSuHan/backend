package com.susuhan.travelpick.domain.user.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class UserIdNotFoundException : BusinessException(ErrorCode.USER_ID_NOT_FOUND) {

    override fun isNecessaryToLog(): Boolean = false
}