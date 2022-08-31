package com.mohang.infrastructure.passwordencoder

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isNotEqualTo
import strikt.assertions.isTrue

/**
 * Created by ShinD on 2022/09/01.
 */
internal class SecurityMemberPasswordEncoderTest {

    companion object {
        private val passwordEncoder = SecurityMemberPasswordEncoder()
    }

    @Test
    fun `비밀번호 인코딩 테스트`() {

        //given
        val rawPassword = "raw"

        //when
        expectThat(passwordEncoder.encode(rawPassword))
            //then
            .isNotEqualTo(rawPassword)
    }

    @Test
    fun `인코딩된 비밀번호 match 여부 테스트`() {


        //given
        val rawPassword = "raw"
        val encodePassword = passwordEncoder.encode(rawPassword)

        //when
        expectThat(passwordEncoder.matches(rawPassword, encodePassword))
            //then
            .isTrue()

        //when
        expectThat(passwordEncoder.matches(rawPassword+"11", encodePassword))
            //then
            .isFalse()

        //when
        expectThat(passwordEncoder.matches(rawPassword, encodePassword+"11"))
            //then
            .isFalse()
    }

}