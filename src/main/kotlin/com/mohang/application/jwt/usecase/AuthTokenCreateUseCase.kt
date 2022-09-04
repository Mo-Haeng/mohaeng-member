package com.mohang.application.jwt.usecase

import com.mohang.domain.jwt.AuthToken
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple

/**
 * Created by ShinD on 2022/09/02.
 *
 * JWT 라이브러리를 교체할 수 있으므로 인터페이스로 설정하였습니다.
 */
interface AuthTokenCreateUseCase {

    companion object {

        const val AUTH_TOKEN_SUBJECT = "AuthToken"
        const val MEMBER_ID_CLAIM = "memberId"
        const val MEMBER_ROLE_CLAIM = "memberRole"
    }

    fun command(authPrinciple: AuthMemberPrinciple): AuthToken
}