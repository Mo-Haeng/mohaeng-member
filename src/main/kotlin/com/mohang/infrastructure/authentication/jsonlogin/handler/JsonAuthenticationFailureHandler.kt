package com.mohang.infrastructure.authentication.jsonlogin.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohang.configuration.exception.ExceptionResponse
import com.mohang.presentation.util.ResponseUtil
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/05.
 */
class JsonAuthenticationFailureHandler(

    private val objectMapper: ObjectMapper
) : AuthenticationFailureHandler {

    private val log = KotlinLogging.logger {  }

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {

        log.error { "로그인 도중 예외가 발생하였습니다. MESSAGE = [${exception.message}]\n STACK TRACE = [${exception.stackTraceToString()}]" }

        val exceptionResponse = ExceptionResponse(code = 401, message = exception.message ?: "접근 권한이 없거나, 예외가 발생하였습니다.")
        ResponseUtil.sendError(
            json = objectMapper.writeValueAsString(exceptionResponse),
            response = response,
            httpStatus = HttpStatus.FORBIDDEN
        )
    }
}