package com.mohaeng.application.jwt.usecase

import com.mohaeng.domain.jwt.AuthToken
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple

/**
 * Created by ShinD on 2022/09/06.
 */
interface ExtractAuthMemberFromAuthTokenUseCase {

    fun command(authToken: AuthToken): AuthMemberPrinciple?
}