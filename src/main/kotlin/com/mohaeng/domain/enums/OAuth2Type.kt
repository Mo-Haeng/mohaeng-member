package com.mohaeng.domain.enums

/**
 * Created by ShinD on 2022/08/30.
 */
enum class OAuth2Type(val registrationId: String, val oauth2IdName: String) {

    NONE("none", "username"), // 일반 회원가입

    KAKAO("kakao", "id"),

    GOOGLE("google", "email"),

    NAVER("naver", "id"),

    ;

    companion object {

        fun fromRegistrationId(registrationId: String): OAuth2Type =
            when (registrationId) {
                KAKAO.registrationId -> KAKAO
                GOOGLE.registrationId -> GOOGLE
                NAVER.registrationId -> NAVER
                else -> throw RuntimeException("등록되지 않은 OAuth2 서비스입니다.")
            }
    }
}