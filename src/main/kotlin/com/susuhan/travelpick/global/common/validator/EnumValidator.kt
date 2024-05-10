package com.susuhan.travelpick.global.common.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<EnumValid, String> {

    private lateinit var annotation: EnumValid

    override fun initialize(constraintAnnotation: EnumValid) {
        this.annotation = constraintAnnotation
    }

    override fun isValid(value: String, context: ConstraintValidatorContext): Boolean {
        val enumValues = this.annotation.enumClass.java.enumConstants ?: return false
        return enumValues.any { enumValue -> value.equals(enumValue.toString(), true) }
    }
}