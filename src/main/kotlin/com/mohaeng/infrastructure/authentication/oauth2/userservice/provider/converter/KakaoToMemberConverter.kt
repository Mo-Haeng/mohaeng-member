package com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter

import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.enums.OAuth2Type.KAKAO
import com.mohaeng.domain.enums.Role
import com.mohaeng.domain.member.Member
import com.mohaeng.domain.member.OAuth2LoginId

/**
 * Created by ShinD on 2022/09/04.
 */
class KakaoToMemberConverter : OAuth2AttributesToMemberConverter {

    override fun support(oAuth2Type: OAuth2Type) =
        KAKAO == oAuth2Type


    override fun convert(attributes: Map<String, Any>): Member {
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
            nickname = nickname,
            email = email,
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
}
