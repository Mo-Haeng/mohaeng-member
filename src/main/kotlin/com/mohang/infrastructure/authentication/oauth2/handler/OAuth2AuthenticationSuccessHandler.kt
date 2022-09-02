package com.mohang.infrastructure.authentication.oauth2.handler

import com.mohang.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository.Companion.SESSION_REDIRECT_ATTR_NAME
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AuthenticationSuccessHandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {

        var redirectUri = request.getSession(false).getAttribute(SESSION_REDIRECT_ATTR_NAME) as String
        request.session.invalidate()

        val principal = authentication.principal as AuthMemberPrinciple
        println(principal.id)
        println(principal.role)
        println(principal.oauth2LoginId)
        println(principal.password)


        redirectUri = UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("accessToken", "AccessToken")
            .build().toUriString()

        response.sendRedirect(redirectUri)
    }
}