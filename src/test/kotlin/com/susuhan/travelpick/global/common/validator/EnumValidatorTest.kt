package com.susuhan.travelpick.global.common.validator

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.validation.ConstraintValidatorContext

class EnumValidatorTest : DescribeSpec({

    lateinit var enumValidator: EnumValidator
    lateinit var constraintValidatorContext: ConstraintValidatorContext

    beforeTest {
        val enumValid = mockk<EnumValid> {
            every { enumClass.java } returns TestEnum::class.java
        }
        constraintValidatorContext = mockk<ConstraintValidatorContext>()
        enumValidator = EnumValidator()
        enumValidator.initialize(enumValid)
    }

    describe("validation") {
        context("유효한 대문자 enum 값을 검증하면") {
            it("true를 반환해야 한다.") {
                val result = enumValidator.isValid(
                    value = TestEnum.VALUE1.toString().uppercase(),
                    context = constraintValidatorContext,
                )

                result shouldBe true
            }
        }

        context("유효한 소문자 enum 값을 검증하면") {
            it("true를 반환해야 한다.") {
                val result = enumValidator.isValid(
                    value = TestEnum.VALUE1.toString().lowercase(),
                    context = constraintValidatorContext,
                )

                result shouldBe true
            }
        }

        context("유효하지 않은 enum 값을 검증하면") {
            it("false를 반환해야 한다.") {
                val result = enumValidator.isValid(
                    value = "invalid-value",
                    context = constraintValidatorContext,
                )

                result shouldBe false
            }
        }

        context("빈 문자열 값을 검증하면") {
            it("false를 반환해야 한다.") {
                val result = enumValidator.isValid(
                    value = "",
                    context = constraintValidatorContext,
                )

                result shouldBe false
            }
        }
    }
})

enum class TestEnum {
    VALUE1,
    VALUE2,
    VALUE3,
}
