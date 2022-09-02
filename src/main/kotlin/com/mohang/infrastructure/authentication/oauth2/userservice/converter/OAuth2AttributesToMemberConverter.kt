package com.mohang.infrastructure.authentication.oauth2.userservice.converter

import com.mohang.domain.enums.OAuth2Type
import com.mohang.domain.enums.OAuth2Type.*
import com.mohang.domain.enums.Role
import com.mohang.domain.member.Member
import com.mohang.domain.member.OAuth2LoginId

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AttributesToMemberConverter {

    fun convert(oAuth2Type: OAuth2Type, attributes: Map<String, Any>): Member =
        when (oAuth2Type) {
            KAKAO -> convertKakao(attributes)
            GOOGLE -> convertGoogle(attributes)
            NAVER -> convertNaver(attributes)
            else -> throw RuntimeException("등록되지 않은 OAuth2 서비스입니다.")
        }

    /**
     * 카카오 회원 정보를 Member 객체로 변환
     */
    private fun convertKakao(attributes: Map<String, Any>): Member {

        val oAuth2LoginId = OAuth2LoginId(oauth2Type = KAKAO, value = attributes[KAKAO.oauth2IdName].toString())

        val kakaoAccountMap = getKakaoAccountMap(attributes)
        val email = getEmail(kakaoAccountMap)

        val kakaoProfileMap = getKakaoProfileMap(kakaoAccountMap)
        val nickname = getNickname(kakaoProfileMap)
        val profileImagePath = getProfileImagePath(kakaoProfileMap)

        return Member(
            role = Role.BASIC,
            oauth2LoginId = oAuth2LoginId,
            name = nickname,
            email = email,
            nickname = nickname,
            profileImagePath = profileImagePath
        )
    }

    private fun getKakaoAccountMap(attributes: Map<String, Any>) =
        attributes["kakao_account"] as Map<*, *>

    private fun getEmail(kakaoAccountMap: Map<*, *>): String? {
        var email = kakaoAccountMap["email"] as String?
        val isEmailValid = kakaoAccountMap["is_email_valid"] as Boolean
        if (!isEmailValid) email = null
        return email
    }

    private fun getKakaoProfileMap(kakaoAccountMap: Map<*, *>) = kakaoAccountMap["profile"] as Map<*, *>

    private fun getProfileImagePath(kakaoProfileMap: Map<*, *>) = kakaoProfileMap["profile_image_url"] as String

    private fun getNickname(kakaoProfileMap: Map<*, *>) = kakaoProfileMap["nickname"] as String

    private fun convertGoogle(attributes: Map<String, Any>): Member {
        TODO("Not yet implemented")
    }

    private fun convertNaver(attributes: Map<String, Any>): Member {
        TODO("Not yet implemented")
    }
}