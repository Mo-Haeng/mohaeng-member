package com.mohaeng.infrastructure.authentication.jwt.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohaeng.configuration.exception.ExceptionResponse
import com.mohaeng.presentation.util.ResponseUtil
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/06.
 */
class JwtAuthenticationFailureHandler(

    private val objectMapper: ObjectMapper,

    ) {

    private val log = KotlinLogging.logger {  }


    fun failureAuthentication(response: HttpServletResponse, ex: Exception) {
        when (ex) {
            //예상한 범위 내의 오류
            is AuthException -> {
                log.error { "인증 예외가 발생하였습니다.  MESSAGE = [${ex.message}] \n STACK TRACE = [${ex.stackTraceToString()}]" }

                val exceptionResponse = ExceptionResponse(code = 401, message = ex.message ?: "인증에 실패하였습니다.")

                ResponseUtil.sendError(
                    json = objectMapper.writeValueAsString(exceptionResponse),
                    response = response,
                    httpStatus = HttpStatus.UNAUTHORIZED
                )
            }

            //예상하지 못한 오류
            else -> {
                log.error { "예측하지 못한 예외 발생 - message : [${ex.message}], cause : [${ex.cause}] \n stackTrace : ${ex.stackTraceToString()}" }
                val exceptionResponse = ExceptionResponse(code = 500, message = ex.message ?: "서버에서 예상하지 못한 오류가 발생하여 인증에 실패하였습니다.")

                ResponseUtil.sendError(
                    json = objectMapper.writeValueAsString(exceptionResponse),
                    response = response,
                    httpStatus = HttpStatus.UNAUTHORIZED
                )
            }
        }
    }
}