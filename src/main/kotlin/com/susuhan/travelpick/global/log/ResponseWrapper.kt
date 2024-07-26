package com.susuhan.travelpick.global.log

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.util.ContentCachingResponseWrapper

class ResponseWrapper(response: HttpServletResponse) : ContentCachingResponseWrapper(response)
