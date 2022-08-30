package com.mohang.infrastructure.persistence

import com.mohang.domain.enums.SocialLoginType
import com.mohang.domain.enums.SocialLoginType.*
import com.mohang.domain.member.SocialLoginId
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
        val general = SocialLoginId(socialLoginType = NONE, value = "sample email")
        val google = SocialLoginId(socialLoginType = GOOGLE, value = "sample google email")
        val kakao = SocialLoginId(socialLoginType = KAKAO, value = "sample kakao Id")
        val naver = SocialLoginId(socialLoginType = NAVER, value = "sample id")

        val generalMember = notSavedMember(socialLoginId = general)
        val googleMember = notSavedMember(socialLoginId = google)
        val kakaoMember = notSavedMember(socialLoginId = kakao)
        val naverMember = notSavedMember(socialLoginId = naver)

        memberRepository.saveAll(listOf(generalMember, googleMember, kakaoMember, naverMember))


        //when, then
        expectThat(memberRepository.findBySocialLoginId(general)).isEqualTo(generalMember)
        expectThat(memberRepository.findBySocialLoginId(google)).isEqualTo(googleMember)
        expectThat(memberRepository.findBySocialLoginId(kakao)).isEqualTo(kakaoMember)
        expectThat(memberRepository.findBySocialLoginId(naver)).isEqualTo(naverMember)
    }
}