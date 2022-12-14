package com.mohaeng.application.jwt.usecase

import com.mohaeng.domain.jwt.AuthToken
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple

/**
 * Created by ShinD on 2022/09/02.
 *
 * JWT 라이브러리를 교체할 수 있으므로 인터페이스로 설정하였습니다.
 */
interface AuthTokenCreateUseCase {

    fun command(authPrinciple: AuthMemberPrinciple): AuthToken
}