package com.susuhan.travelpick.global.kakao.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class KakaoServerException(errorMessage: String = "")
    : BusinessException(ErrorCode.KAKAO_SERVER_ERROR, errorMessage) {

    override fun isNecessaryToLog(): Boolean = true
}