package com.mohang.infrastructure.authentication.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohang.configuration.exception.ExceptionResponse
import com.mohang.presentation.util.ResponseUtil
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/03.
 */
class SendErrorAccessDeniedHandler(

    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    private val log = KotlinLogging.logger {  }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {

        log.error { "인가 예외가 발생하였습니다.  MESSAGE = [${accessDeniedException.message}] " }

        val exceptionResponse = ExceptionResponse(code = 401, message = accessDeniedException.message ?: "접근 권한이 없거나, 예외가 발생하였습니다.")
        ResponseUtil.sendError(
            json = objectMapper.writeValueAsString(exceptionResponse),
            response = response,
            httpStatus = HttpStatus.FORBIDDEN
        )
    }
}