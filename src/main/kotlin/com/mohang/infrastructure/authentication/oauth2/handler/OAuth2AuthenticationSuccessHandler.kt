package com.mohang.infrastructure.authentication.oauth2.handler

import com.mohang.application.jwt.AuthTokenSender
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohang.infrastructure.authentication.exception.NoRedirectUrlException
import com.mohang.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository.Companion.SESSION_REDIRECT_ATTR_NAME
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AuthenticationSuccessHandler(

    private val authTokenCreateUseCase: AuthTokenCreateUseCase,

    // 토큰 반환 담당
    private val authTokenSender: AuthTokenSender = AuthTokenSender(),

) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {

        val nullableRedirectUrl = request.getSession(false)
            ?.getAttribute(SESSION_REDIRECT_ATTR_NAME)

        // 필수!!
        request.session.invalidate()

        if (nullableRedirectUrl == null) {
            throw NoRedirectUrlException("redirect_url을 설정해 주셔야 합니다.")
        }

        val redirectUrl = nullableRedirectUrl as String

        val principal = authentication.principal as AuthMemberPrinciple

        val authToken = authTokenCreateUseCase.command(principal)

        authTokenSender.sendByRedirectQueryString(response = response, redirectUrl = redirectUrl, authToken = authToken)
    }
}