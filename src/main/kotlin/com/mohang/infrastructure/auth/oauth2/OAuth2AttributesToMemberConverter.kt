package com.mohang.infrastructure.auth.oauth2

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
        when(oAuth2Type) {
            KAKAO -> convertKakao(attributes)
            GOOGLE -> convertGoogle(attributes)
            NAVER -> convertNaver(attributes)
            else -> throw RuntimeException("등록되지 않은 OAuth2 서비스입니다.")
    }

    private fun convertKakao(attributes: Map<String, Any>): Member {

        val oAuth2LoginId = OAuth2LoginId(oauth2Type = KAKAO, value = attributes[KAKAO.oauth2IdName].toString())
        val kakaoAccountMap = attributes["kakao_account"] as Map<*, *>

        val isEmailValid = kakaoAccountMap["is_email_valid"] as Boolean

        var email = kakaoAccountMap["email"] as String?
        if (!isEmailValid) email = null

        val kakaoProfileMap = kakaoAccountMap["profile"] as Map<*, *>
        val nickname = kakaoProfileMap["nickname"] as String
        val profileImagePath = kakaoProfileMap["profile_image_url"] as String

        return Member(
            role = Role.BASIC,
            oauth2LoginId = oAuth2LoginId,
            name = nickname,
            email = email,
            nickname = nickname,
            profileImagePath = profileImagePath
        )
    }

    private fun convertGoogle(attributes: Map<String, Any>): Member {
        TODO("Not yet implemented")
    }

    private fun convertNaver(attributes: Map<String, Any>): Member {
        TODO("Not yet implemented")
    }

}