package com.mohang.infrastructure.persistence

import com.mohang.domain.RegistrationId
import com.mohang.domain.enums.RegistrationType.*
import com.mohang.infrastructure.fixture.MemberFixture.notSavedMember
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

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
        val general = RegistrationId(registrationType = GENERAL, registrationIdValue = "sample email")
        val google = RegistrationId(registrationType = GOOGLE, registrationIdValue = "sample google email")
        val kakao = RegistrationId(registrationType = KAKAO, registrationIdValue = "sample kakao Id")
        val naver = RegistrationId(registrationType = NAVER, registrationIdValue = "sample id")

        val generalMember = notSavedMember(registrationId = general)
        val googleMember = notSavedMember(registrationId = google)
        val kakaoMember = notSavedMember(registrationId = kakao)
        val naverMember = notSavedMember(registrationId = naver)

        memberRepository.saveAll(listOf(generalMember, googleMember, kakaoMember, naverMember))


        //when
        val findGeneral = memberRepository.findByRegistrationId(general)
        val findGoogle = memberRepository.findByRegistrationId(google)
        val findKakao = memberRepository.findByRegistrationId(kakao)
        val findNaver = memberRepository.findByRegistrationId(naver)

        //then
        assertThat(findGeneral).isEqualTo(generalMember)
        assertThat(findGoogle).isEqualTo(googleMember)
        assertThat(findKakao).isEqualTo(kakaoMember)
        assertThat(findNaver).isEqualTo(naverMember)
    }
}