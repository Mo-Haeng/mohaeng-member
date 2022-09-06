package com.mohang.presentation.argumentresolver.auth

import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import com.mohang.presentation.model.AuthMember
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * Created by ShinD on 2022/09/06.
 */
@Component
class AuthMemberArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {

        val hasAuthAnnotation = parameter.hasParameterAnnotation(Auth::class.java)

        val hasAuthMemberPrincipleType = AuthMember::class.java.isAssignableFrom(parameter.parameterType)

        return hasAuthAnnotation && hasAuthMemberPrincipleType
    }





    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): AuthMember? {

        val authentication = SecurityContextHolder.getContext().authentication

        if(authentication == null || !authentication.isAuthenticated ) return null

        val authMemberPrinciple = authentication.principal as AuthMemberPrinciple

        return AuthMember(
            id = authMemberPrinciple.id,
            role = authMemberPrinciple.role
        )
    }
}