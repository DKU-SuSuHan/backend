package com.susuhan.travelpick.global.common.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class DateUtilsTest : StringSpec({

    "이번 연도의 날짜가 주어지면 'M월 d일' 형식으로 반환해야 한다." {
        val currentDate = LocalDate.now()
        val expectResult = "${currentDate.monthValue}월 ${currentDate.dayOfMonth}일"
        val result = DateUtils.parse(currentDate)

        result shouldBe expectResult
    }

    "이전 연도의 날짜가 주어지면 'yyyy년 M월 d일' 형식으로 반환해야 한다." {
        val oneYearAgoDate = LocalDate.now().minusYears(1)
        val expectResult = "${oneYearAgoDate.year}년 ${oneYearAgoDate.monthValue}월 ${oneYearAgoDate.dayOfMonth}일"
        val result = DateUtils.parse(oneYearAgoDate)

        result shouldBe expectResult
    }
})
