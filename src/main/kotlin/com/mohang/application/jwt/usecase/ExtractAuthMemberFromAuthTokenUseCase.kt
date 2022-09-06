package com.mohang.application.jwt.usecase

import com.mohang.domain.jwt.AuthToken
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple

/**
 * Created by ShinD on 2022/09/06.
 */
interface ExtractAuthMemberFromAuthTokenUseCase {

    fun command(authToken: AuthToken): AuthMemberPrinciple?
}