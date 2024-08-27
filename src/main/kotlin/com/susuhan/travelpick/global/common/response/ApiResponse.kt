package com.susuhan.travelpick.global.common.response

data class ApiResponse<T>(
    val success: Boolean,
    val status: Int,
    val data: T,
) {

    companion object {
        fun <T> success(status: Int, data: T?) = ApiResponse(true, status, data)
        fun <T> fail(status: Int, data: T?) = ApiResponse(false, status, data)
    }
}
