package com.mohaeng.infrastructure.authentication.jsonlogin.provider.usecase

import com.mohaeng.domain.enums.OAuth2Type.NONE
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.fixture.MemberFixture
import com.mohaeng.fixture.MemberFixture.savedMember
import com.mohaeng.infrastructure.persistence.MemberRepository
import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.UsernameNotFoundException
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

/**
 * Created by ShinD on 2022/09/01.
 */
internal class LoadMemberUseCaseTest {

    private val memberRepository = mockkClass(MemberRepository::class)

    private val loadMemberUseCase: LoadMemberUseCase by lazy {
        LoadMemberUseCase(memberRepository)
    }

    @Test
    fun `회원 정보 조회 성공`() {

        //given
        val member = savedMember()
        val username = "example"
        val oAuth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)
        every { memberRepository.findByOauth2LoginId(any()) } returns member

        val authMemberPrinciple = MemberFixture.authBasicMemberPrinciple()

        //when
        expectThat(
            loadMemberUseCase.command(oAuth2LoginId).id
        ) {
            //then
            isEqualTo(authMemberPrinciple.id)
        }
    }

    @Test
    fun `회원 정보 조회 실패 시 예외 발생`() {

        //given
        val username = "example"
        val oAuth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)
        every { memberRepository.findByOauth2LoginId(any()) } returns null

        //when
        expectThrows<UsernameNotFoundException> {
            loadMemberUseCase.command(oAuth2LoginId)
        }.message.isEqualTo("일치하는 회원이 없습니다.")
    }
}