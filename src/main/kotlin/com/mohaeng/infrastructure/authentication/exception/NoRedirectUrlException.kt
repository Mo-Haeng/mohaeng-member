package com.mohaeng.infrastructure.authentication.exception

import org.springframework.security.core.AuthenticationException

/**
 * Created by ShinD on 2022/09/03.
 */
class NoRedirectUrlException(
    message: String
) : AuthenticationException(message) {
}