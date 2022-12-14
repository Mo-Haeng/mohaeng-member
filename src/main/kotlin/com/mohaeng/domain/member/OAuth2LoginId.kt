package com.mohaeng.domain.member

import com.mohaeng.domain.enums.OAuth2Type
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated

/**
 * Created by ShinD on 2022/08/30.
 */
@Embeddable
data class OAuth2LoginId(

    @Enumerated(STRING)
    val oauth2Type: OAuth2Type, // 회원가입 타입 - NONE(일반 회원가입), NAVER, GOOGLE, KAKAO

    /**
     * 등록 타입에서 제공하는 식별자에 해당하는 값
     *
     * Example: KAKAO 회원가입, kakaoId가 123인경우
     *  - socialLoginType: [KAKAO]
     *  - value: 123
     *
     * Example: NONE 회원가입, username이 123인경우
     *  - socialLoginType: [NONE]
     *  - value: 123
     */
    @Column(name = "oauth2_id_value")
    val value: String,
)