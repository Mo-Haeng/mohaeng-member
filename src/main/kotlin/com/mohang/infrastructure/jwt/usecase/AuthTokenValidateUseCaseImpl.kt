package com.mohang.infrastructure.jwt.usecase

import com.mohang.application.jwt.usecase.AuthTokenValidateUseCase
import com.mohang.domain.jwt.AuthToken
import com.mohang.infrastructure.jwt.usecase.properties.JwtProperties

/**
 * Created by ShinD on 2022/09/02.
 */
class AuthTokenValidateUseCaseImpl(

    private val jwtProperties: JwtProperties,

    ) : AuthTokenValidateUseCase {

    override fun command(authToken: AuthToken): Boolean {

        TODO("AuthTokenValidateUseCaseImpl.command")
    }
}