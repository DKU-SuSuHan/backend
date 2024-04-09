package com.susuhan.travelpick.global.exception

import com.susuhan.travelpick.global.exception.dto.ErrorResponse
import com.susuhan.travelpick.global.exception.dto.FieldError
import com.susuhan.travelpick.global.exception.dto.ValidationErrorResponse
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 서비스에서 발생하는 모든 예외를 처리해 적절한 응답 형태로 반환하는
 * ResponseEntityExceptionHandler 클래스를 상속받아 Spring MVC 관련 예외를 처리해줌
 */
@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponse> {
        if (ex.isNecessaryToLog()) {
            logger.error("[Business_Exception]: ${getExceptionStackTraceToString(ex)}");
        }

        return ResponseEntity
            .status(ex.httpStatus)
            .body(ErrorResponse(
                ex.code, ex.errorMessage
            ))
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ) : ResponseEntity<Any> {
        logger.error("[Method_Argument_Not_Valid_Exception]: ${getExceptionStackTraceToString(ex)}");

        val errors = ex.bindingResult.fieldErrors.map { error ->
            FieldError(
                error.field,
                error.rejectedValue?.toString() ?: "",
                error?.defaultMessage ?: ""
            )
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ValidationErrorResponse(
                ErrorCode.METHOD_ARGUMENT_NOT_VALID.code,
                ErrorCode.METHOD_ARGUMENT_NOT_VALID.errorMessage,
                errors
            ))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleBusinessException(ex: ConstraintViolationException): ResponseEntity<ValidationErrorResponse> {
        logger.error("[Constraint_Violation_Exception]: ${getExceptionStackTraceToString(ex)}");

        val errors = ex.constraintViolations.map { violation ->
            FieldError(
                getFieldName(violation),
                violation.invalidValue.toString(),
                violation.message
            )
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ValidationErrorResponse(
                ErrorCode.CONSTRAINT_VIOLATION.code,
                ErrorCode.CONSTRAINT_VIOLATION.errorMessage,
                errors
            ))
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("[Spring_MVC_Standard_Exception]: ${getExceptionStackTraceToString(ex)}");

        return ResponseEntity
            .status(statusCode.value())
            .body(ErrorResponse(
                ErrorCode.UNHANDLED.code, ex?.message
            ))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("[Unhandled_Exception]: ${getExceptionStackTraceToString(ex)}");

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(
                ErrorCode.UNHANDLED.code, ex?.message
            ))
    }

    companion object {
        private fun getFieldName(violation: ConstraintViolation<*>): String {
            val propertyPath = violation.propertyPath.toString()
            val dotIdx = propertyPath.lastIndexOf(".")
            return propertyPath.substring(dotIdx + 1)
        }

        private fun getExceptionStackTraceToString(ex: Exception): String {
            val stringWriter = StringWriter()
            ex.printStackTrace(PrintWriter(stringWriter))
            return stringWriter.toString()
        }
    }
}