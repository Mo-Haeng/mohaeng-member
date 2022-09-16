package com.mohaeng.infrastructure.authentication.oauth2.userservice.provider

import com.mohaeng.domain.enums.OAuth2Type.KAKAO
import com.mohaeng.fixture.MemberFixture.notSavedMember
import com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter.OAuth2AttributesToMemberConverter
import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/09/01.
 */
internal class OAuth2AttributesToMemberConverterProviderTest {

    /**
     * convert 시 kakao, google, naver로 적절히 변환되는가
     */
    private val converter = mockkClass(OAuth2AttributesToMemberConverter::class)
    private val provider = OAuth2AttributesToMemberConverterProvider(listOf(converter))

    @Test
    fun `converter를 호출하여 회원 반환`() {

        //given
        val member = notSavedMember()
        every { converter.support(KAKAO) } returns true
        every { converter.convert(any()) } returns member

        //when
        val convertedMember = provider.convert(KAKAO, hashMapOf())

        //then
        expectThat(convertedMember) {
            isEqualTo(member)
        }
    }
}