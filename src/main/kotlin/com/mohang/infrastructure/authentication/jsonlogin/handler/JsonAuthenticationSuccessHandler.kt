package com.mohang.infrastructure.authentication.jsonlogin.handler

import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class JsonAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(

        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,

        ) {
        val principal = authentication.principal as AuthMemberPrinciple
        println(principal.id)
        println(principal.role)
        println(principal.oauth2LoginId)
        println(principal.password)
        //TODO("JWT 발급")
    }
}