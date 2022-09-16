package com.mohaeng.presentation.advice

import com.mohaeng.configuration.exception.ExceptionResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Created by ShinD on 2022/09/05.
 */
@RestControllerAdvice
class ExceptionControllerAdvice {

    private val log = KotlinLogging.logger { }


    @ExceptionHandler(BindException::class, HttpMessageNotReadableException::class)
    fun handleBindException(ex: Exception): ResponseEntity<ExceptionResponse> {

        log.error { "Json 혹은 요청 파라미터의 형식이 올바르지 않습니다. \n - message : [${ex.message}]\n - cause : [${ex.cause}]\n stackTrace : ${ex.stackTraceToString()}" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse(code = 400, message = "Json 혹은 요청 파라미터의 형식이 올바르지 않습니다."))
    }



    @ExceptionHandler(RuntimeException::class)
    fun handleException(ex: RuntimeException): ResponseEntity<ExceptionResponse> {

        log.error { "예외 발생 - message : [${ex.message}], cause : [${ex.cause}] \n stackTrace : ${ex.stackTraceToString()}" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ExceptionResponse(code = 400, message = ex.message ?: "예외 발생"))
    }



    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ExceptionResponse> {

        log.error { "예측하지 못한 예외 발생 - message : [${ex.message}], cause : [${ex.cause}] \n stackTrace : ${ex.stackTraceToString()}" }

        return ResponseEntity
            .status(500)
            .body(ExceptionResponse(code = 500, message = ex.message ?: "예측하지 못한 예외가 발생하였습니다."))
    }
}