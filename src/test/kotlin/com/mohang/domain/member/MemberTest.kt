package com.mohang.domain.member

import com.mohang.fixture.MemberFixture.notSavedMember
import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/08/30.
 */
internal class MemberTest {

    private val encoder: MemberPasswordEncoder = mockkClass(MemberPasswordEncoder::class)

    @Test
    fun `비밀번호 인코딩 동작 테스트`() {

        //given
        val rawPassword = "raw"
        val member = notSavedMember(password = rawPassword)
        val encodedPassword = "encode"
        every { encoder.encode(rawPassword) } returns encodedPassword


        //when
        member.passwordEncoding(encoder)

        //then
        expectThat(member.password).isEqualTo(encodedPassword)
    }
}