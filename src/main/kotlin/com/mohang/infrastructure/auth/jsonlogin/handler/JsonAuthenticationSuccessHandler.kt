package com.mohang.infrastructure.auth.jsonlogin.handler

import com.mohang.infrastructure.auth.jsonlogin.principle.AuthMember
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
        println(authentication)
        println(authentication.principal)
        val member = authentication.principal as AuthMember
        println(member.id)
        println(member.role)
        println(member.username)
        //TODO("JWT 발급")
    }
}