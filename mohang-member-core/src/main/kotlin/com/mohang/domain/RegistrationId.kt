package com.mohang.domain

import com.mohang.domain.enums.RegistrationType
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

/**
 * Created by ShinD on 2022/08/30.
 */
@Embeddable
class RegistrationId(

    @Enumerated(EnumType.STRING)
    val registrationType: RegistrationType, // 등록 타입 - GENERAL(회원가입), KAKAO, GOOGLE, NAVER


    /**
     * 등록 타입에서 제공하는 식별자에 해당하는 값
     *
     * Example: KAKAO 로그인, kakaoId가 123인경우
     *  - registrationType: [KAKAO]
     *  - registrationIdValue: 123
     *
     * Example: GENERAL 로그인,username이 123인경우
     *  - registrationType: [GENERAL]
     *  - registrationIdValue: 123
     */
    var registrationIdValue: String,

) {

}