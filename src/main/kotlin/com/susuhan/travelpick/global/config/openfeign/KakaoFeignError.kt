package com.susuhan.travelpick.global.config.openfeign

import com.susuhan.travelpick.global.common.logger
import com.susuhan.travelpick.global.kakao.exception.KakaoServerException
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.util.StreamUtils
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets

class KakaoFeignError : ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): Exception {
        try {
            response?.body()?.asInputStream()?.use { bodyIs ->
                val errorJsonMessage = StreamUtils.copyToString(bodyIs, StandardCharsets.UTF_8)
                return KakaoServerException(errorJsonMessage.replace("\"", "\'"))
            }
        } catch (ex: IOException) {
            logger().error("[Kakao_Feign_I/O_Exception]: 에러 응답을 처리하는 과정에서 에러가 발생했습니다.")

            return Exception(ex?.message)
        }
        return KakaoServerException()
    }
}