package com.mohang.infrastructure.auth.oauth2.handler

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
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
        //TODO("JWT 발급")
    }
}