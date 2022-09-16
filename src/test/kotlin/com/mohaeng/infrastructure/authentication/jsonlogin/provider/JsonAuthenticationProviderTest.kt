package com.mohaeng.infrastructure.authentication.jsonlogin.provider

import com.mohaeng.domain.member.MemberPasswordEncoder
import com.mohaeng.fixture.MemberFixture.authBasicMemberPrinciple
import com.mohaeng.fixture.MemberFixture.savedMember
import com.mohaeng.infrastructure.authentication.jsonlogin.authentication.JsonUsernamePasswordToken
import com.mohaeng.infrastructure.authentication.jsonlogin.provider.usecase.LoadMemberUseCase
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import strikt.assertions.message

/**
 * Created by ShinD on 2022/09/01.
 */
internal class JsonAuthenticationProviderTest {

    private val encoder = mockkClass(MemberPasswordEncoder::class)
    private val loadMemberUseCase = mockkClass(LoadMemberUseCase::class)

    private val jsonAuthenticationProvider: JsonAuthenticationProvider by lazy {
        JsonAuthenticationProvider(encoder, loadMemberUseCase)
    }

    @Test
    fun `인증 성공시 AuthMemberPrinciple이 담긴 JsonUsernamePasswordToken 반환`() {

        //given
        val member = savedMember()
        val memberDetails = authBasicMemberPrinciple()

        every { loadMemberUseCase.command(any()) } returns memberDetails
        every { encoder.matches(any(), any()) } returns true

        val jupt = JsonUsernamePasswordToken(
            principal = member.oauth2LoginId.value,
            credentials = member.password,
            role = member.role
        )

        //when
        val authenticate = jsonAuthenticationProvider.authenticate(jupt)

        //then
        expectThat(authenticate.javaClass.isAssignableFrom(JsonUsernamePasswordToken::class.java)) {
            isTrue()
        }

        val returnedToken = authenticate as JsonUsernamePasswordToken
        expectThat(returnedToken.principal.javaClass.isAssignableFrom(AuthMemberPrinciple::class.java)) {
            isTrue()
        }

        val authMemberPrinciple = returnedToken.principal as AuthMemberPrinciple

        with(authMemberPrinciple) {
            expectThat(id) { isEqualTo(member.id) }
            expectThat(password) { isEqualTo(null) }
            expectThat(oauth2LoginId) { isEqualTo(member.oauth2LoginId) }
            expectThat(role) { isEqualTo(member.role) }
        }
    }

    @Test
    fun `비밀번호 일치하지 않은 경우 예외`() {

        //given
        val member = savedMember()
        val memberDetails = authBasicMemberPrinciple()

        every { loadMemberUseCase.command(any()) } returns memberDetails
        every { encoder.matches(any(), any()) } returns false

        val jupt = JsonUsernamePasswordToken(
            principal = member.oauth2LoginId.value,
            credentials = member.password,
            role = member.role
        )

        //when
        expectThrows<BadCredentialsException> { jsonAuthenticationProvider.authenticate(jupt) }
            .message.isEqualTo("비밀번호가 잘못되었습니다")
    }

    @Test
    fun `JsonUsernamePasswordToken에 담긴 인증 정보 support true`() {

        with(jsonAuthenticationProvider) {
            expectThat(supports(JsonUsernamePasswordToken::class.java)) { isTrue() }
            expectThat(supports(UsernamePasswordAuthenticationToken::class.java)) { isFalse() }
        }
    }
}