package com.susuhan.travelpick.global.common.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class JsonProcessException : BusinessException(ErrorCode.JSON_PROCESS_ERROR) {

    override fun isNecessaryToLog(): Boolean = false
}
