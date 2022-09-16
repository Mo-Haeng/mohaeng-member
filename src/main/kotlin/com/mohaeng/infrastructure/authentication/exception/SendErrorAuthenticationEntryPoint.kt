package com.mohaeng.infrastructure.authentication.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohaeng.configuration.exception.ExceptionResponse
import com.mohaeng.presentation.util.ResponseUtil
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/03.
 */
class SendErrorAuthenticationEntryPoint(

    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {

    private val log = KotlinLogging.logger {  }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {

        log.error { "인증 예외가 발생하였습니다.  MESSAGE = [${authException.message}] " }

        val exceptionResponse = ExceptionResponse(code = 401, message = authException.message ?: "인증에 실패하였습니다.")

        ResponseUtil.sendError(
            json = objectMapper.writeValueAsString(exceptionResponse),
            response = response,
            httpStatus = HttpStatus.UNAUTHORIZED
        )
    }
}