package com.mohang.domain.enums

/**
 * Created by ShinD on 2022/08/30.
 */
enum class RegistrationType(registrationIdName: String) {

    GENERAL("username"), // 일반 회원가입

    KAKAO("kakaoId"),

    GOOGLE("email"),

    NAVER("id"),

    ;
}