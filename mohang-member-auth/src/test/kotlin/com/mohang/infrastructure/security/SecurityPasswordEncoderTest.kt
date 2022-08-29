package com.mohang.infrastructure.security

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import strikt.api.expectThat
import strikt.assertions.isNotEqualTo
import strikt.assertions.isTrue

/**
 * Created by ShinD on 2022/08/30.
 */
internal class SecurityPasswordEncoderTest {

    private val passwordEncoder: PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    private val securityPasswordEncoder: SecurityPasswordEncoder = SecurityPasswordEncoder(passwordEncoder)

    @Test
    fun `비밀번호 암호화 `() {

        //given
        val password = "sample"

        //when
        val encodePassword = securityPasswordEncoder.encode(password)

        //then
        expectThat(encodePassword).isNotEqualTo(password)
        expectThat(passwordEncoder.matches(password, encodePassword)).isTrue()
    }

    @Test
    fun `비밀번호 일치여부 확인 `() {

        //given

        //when

        //then
    }
}