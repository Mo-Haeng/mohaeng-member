package com.mohaeng.infrastructure.persistence

import com.mohaeng.domain.enums.OAuth2Type.*
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.fixture.MemberFixture.notSavedMember
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

        val generalMember = notSavedMember(oauth2LoginId = general, email = "1")
        val googleMember = notSavedMember(oauth2LoginId = google, email = "2")
        val kakaoMember = notSavedMember(oauth2LoginId = kakao, email = "3")
        val naverMember = notSavedMember(oauth2LoginId = naver, email = "4")

        memberRepository.saveAll(listOf(generalMember, googleMember, kakaoMember, naverMember))


        //when, then
        expectThat(memberRepository.findByOauth2LoginId(general)).isEqualTo(generalMember)
        expectThat(memberRepository.findByOauth2LoginId(google)).isEqualTo(googleMember)
        expectThat(memberRepository.findByOauth2LoginId(kakao)).isEqualTo(kakaoMember)
        expectThat(memberRepository.findByOauth2LoginId(naver)).isEqualTo(naverMember)
    }
}