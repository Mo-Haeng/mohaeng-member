package com.mohang.application.jwt.usecase

import com.mohang.domain.jwt.AuthToken

/**
 * Created by ShinD on 2022/09/02.
 *
 * JWT 라이브러리를 교체할 수 있으므로 인터페이스로 설정하였습니다.
 */
interface AuthTokenValidateUseCase {

    fun command(authToken: AuthToken): Boolean
}