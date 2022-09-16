package com.mohaeng.infrastructure.authentication.oauth2.handler

import com.mohaeng.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository.Companion.SESSION_REDIRECT_ATTR_NAME
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AuthenticationFailureHandler : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {

        val nullableRedirectUrl = request.getSession(false)
            ?.getAttribute(SESSION_REDIRECT_ATTR_NAME)

        // 필수!!
        request.session.invalidate()

        if (nullableRedirectUrl == null) {
            response.sendRedirect("/?error=no_redirect_url")
            return
        }

        var redirectUrl = nullableRedirectUrl as String

        redirectUrl = UriComponentsBuilder.fromUriString(redirectUrl)
            .queryParam("error", exception.message)
            .build().toUriString()

        response.sendRedirect(redirectUrl)
    }
}