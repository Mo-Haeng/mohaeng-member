package com.mohaeng.infrastructure.authentication.oauth2.userservice

import com.mohaeng.domain.enums.OAuth2Type.KAKAO
import com.mohaeng.fixture.MemberFixture
import com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.OAuth2AttributesToMemberConverterProvider
import com.mohaeng.infrastructure.authentication.oauth2.userservice.usecase.OAuth2SignUpUseCase
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue

/**
 * Created by ShinD on 2022/09/01.
 */
internal class OAuth2SignUpLoginMemberServiceTest {


    private val signUpUseCase = mockkClass(OAuth2SignUpUseCase::class)
    private val provider = mockkClass(OAuth2AttributesToMemberConverterProvider::class)
    private val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User> = mockkClass(OAuth2UserService::class) as OAuth2UserService<OAuth2UserRequest, OAuth2User>
    private val oauth2SignUpLoginMemberService = OAuth2SignUpLoginMemberService(signUpUseCase = signUpUseCase, provider  = provider, delegate = delegate)


    @Test
    fun `loaduser 를 통한 회원 인증 정보 조회`() {

        //given
        val userRequest = mockkClass(OAuth2UserRequest::class)
        val oauth2User = mockkClass(OAuth2User::class)

        every { delegate.loadUser(userRequest) } returns oauth2User

        every { userRequest.clientRegistration.registrationId } returns KAKAO.registrationId

        val member = MemberFixture.notSavedMember()
        every { oauth2User.attributes } returns HashMap()
        every { provider.convert(KAKAO, any()) } returns member

        val savedMember = MemberFixture.savedMember()
        every { signUpUseCase.command(member) } returns savedMember

        //when
        val principle = oauth2SignUpLoginMemberService.loadUser(userRequest)

        //then
        expectThat(principle::class.java.isAssignableFrom(AuthMemberPrinciple::class.java)) {
            isTrue()
        }
        val authMemberPrinciple = principle as AuthMemberPrinciple

        with(authMemberPrinciple) {
            expectThat(id) { isEqualTo(savedMember.id) }
            expectThat(role) { isEqualTo(savedMember.role) }
        }
    }
}