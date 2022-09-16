package com.mohaeng.infrastructure.authentication.oauth2.userservice.usecase

import com.mohaeng.application.member.exception.DuplicateEmailException
import com.mohaeng.fixture.MemberFixture
import com.mohaeng.infrastructure.persistence.MemberRepository
import com.mohaeng.mock.MockTransactionTemplate
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/09/01.
 */
internal class OAuth2SignUpUseCaseTest {

    /**
     * 존재하지 않는 경우 회원가입 + 멤버 반환
     * 존재하는 경우 아무것도 X + 멤버 반환
     *
     * 이메일 중복인 경우 예외 발생
     */
    private val memberRepository = mockkClass(MemberRepository::class)
    private val template = MockTransactionTemplate()
    private val oauth2SignUpUseCase = OAuth2SignUpUseCase(memberRepository, template)

    @Test
    fun `OAuth2 회원가입 성공`() {

        //given
        every { memberRepository.findByOauth2LoginId(any()) } returns null
        every { memberRepository.findByEmail(any()) } returns null
        val notSavedMember = MemberFixture.notSavedMember()
        every { memberRepository.save(any()) } returns notSavedMember // 뭐든 상관없음

        //when
        val savedMember = oauth2SignUpUseCase.command(notSavedMember)

        //then
        verify (exactly = 1) { memberRepository.save(any()) }
    }

    @Test
    fun `이메일 중복 시 회원가입 실패`() {

        //given
        every { memberRepository.findByOauth2LoginId(any()) } returns null
        val member = MemberFixture.notSavedMember()
        every { memberRepository.findByEmail(any()) } returns member

        //when
        expectThrows<DuplicateEmailException> {
            //then
            oauth2SignUpUseCase.command(member)
        }
        verify (exactly = 0) { memberRepository.save(any()) }
    }

    @Test
    fun `이미 가입된 소셜 계정인 경우 회원가입 진행하지 않고 정보만 반환`() {

        //given
        val member = MemberFixture.notSavedMember()
        every { memberRepository.findByOauth2LoginId(any()) } returns member


        //when
        expectThat(oauth2SignUpUseCase.command(member)) {
            isEqualTo(member)
        }

        //then
        verify (exactly = 0){ memberRepository.save(any()) }
    }
}