package com.mohang.infrastructure.authentication.oauth2.handler

import com.mohang.application.jwt.AuthTokenSender
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohang.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository.Companion.SESSION_REDIRECT_ATTR_NAME
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AuthenticationSuccessHandler(

    private val authTokenCreateUseCase: AuthTokenCreateUseCase,

) : AuthenticationSuccessHandler {

    // 토큰 반환 담당
    private val authTokenSender: AuthTokenSender = AuthTokenSender()



    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {

        var redirectUrl = request.getSession(false).getAttribute(SESSION_REDIRECT_ATTR_NAME) as String

        // 필수!!
        request.session.invalidate()


        val principal = authentication.principal as AuthMemberPrinciple

        val authToken = authTokenCreateUseCase.command(principal)

        authTokenSender.sendByRedirectQueryString(response = response, redirectUrl = redirectUrl, authToken = authToken)
    }
}