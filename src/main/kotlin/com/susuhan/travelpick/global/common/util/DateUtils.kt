package com.susuhan.travelpick.global.common.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtils {

    companion object {
        fun parse(date: LocalDate): String {
            val pattern = if (date.year == LocalDate.now().year) {
                "M월 d일"
            } else {
                "yyyy년 M월 d일"
            }
            val formatter = DateTimeFormatter.ofPattern(pattern)
            return date.format(formatter)
        }
    }
}
