package com.mohang.infrastructure.persistence

import com.mohang.domain.enums.OAuth2Type.*
import com.mohang.domain.member.OAuth2LoginId
import com.mohang.fixture.MemberFixture.notSavedMember
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/08/30.
 */

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    fun `findByRegistrationId 작동 테스트`() {

        //given
        val general = OAuth2LoginId(oauth2Type = NONE, value = "sample email")
        val google = OAuth2LoginId(oauth2Type = GOOGLE, value = "sample google email")
        val kakao = OAuth2LoginId(oauth2Type = KAKAO, value = "sample kakao Id")
        val naver = OAuth2LoginId(oauth2Type = NAVER, value = "sample id")

        val generalMember = notSavedMember(socialLoginId = general)
        val googleMember = notSavedMember(socialLoginId = google)
        val kakaoMember = notSavedMember(socialLoginId = kakao)
        val naverMember = notSavedMember(socialLoginId = naver)

        memberRepository.saveAll(listOf(generalMember, googleMember, kakaoMember, naverMember))


        //when, then
        expectThat(memberRepository.findByOauth2LoginId(general)).isEqualTo(generalMember)
        expectThat(memberRepository.findByOauth2LoginId(google)).isEqualTo(googleMember)
        expectThat(memberRepository.findByOauth2LoginId(kakao)).isEqualTo(kakaoMember)
        expectThat(memberRepository.findByOauth2LoginId(naver)).isEqualTo(naverMember)
    }
}