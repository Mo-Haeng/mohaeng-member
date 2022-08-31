package com.mohang.infrastructure.passwordencoder

import com.mohang.domain.member.MemberPasswordEncoder
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by ShinD on 2022/09/01.
 */
class SecurityMemberPasswordEncoder: MemberPasswordEncoder {

    // Security의 패스워드 인코더에 기능 구현을 위임
    private val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()


    override fun encode(rawPassword: String): String =
        passwordEncoder.encode(rawPassword)

    override fun matches(rawPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(rawPassword, encodedPassword)
}