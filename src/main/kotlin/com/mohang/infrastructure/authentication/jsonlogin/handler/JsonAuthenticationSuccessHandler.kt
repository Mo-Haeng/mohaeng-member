package com.mohang.infrastructure.authentication.jsonlogin.handler

import com.mohang.application.jwt.AuthTokenSender
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class JsonAuthenticationSuccessHandler(

    private val authTokenCreateUseCase: AuthTokenCreateUseCase,

    // 토큰 반환 담당
    private val authTokenSender: AuthTokenSender = AuthTokenSender(),

) : AuthenticationSuccessHandler {



    override fun onAuthenticationSuccess(

        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,

        ) {
        val principal = authentication.principal as AuthMemberPrinciple

        val authToken = authTokenCreateUseCase.command(principal)

        authTokenSender.sendByHeader(response = response, authToken = authToken)
        authTokenSender.sendByJson(response = response, authToken = authToken)
    }
}