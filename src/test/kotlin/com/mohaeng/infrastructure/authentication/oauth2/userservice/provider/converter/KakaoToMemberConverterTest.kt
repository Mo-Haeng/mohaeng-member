package com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter

import com.mohaeng.domain.enums.OAuth2Type
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import strikt.assertions.isTrue

/**
 * Created by ShinD on 2022/09/04.
 */
internal class KakaoToMemberConverterTest {

    private val kakaoToMemberConverter = KakaoToMemberConverter()


    @Test
    fun `KAKAO 타입 support`() {

        expectThat(kakaoToMemberConverter.support(OAuth2Type.KAKAO)) {
            isTrue()
        }
    }

    @Test
    fun `KAKAO 에서 제공하는 Attribute를 통해 Convert`() {

        //given
        val attribute: MutableMap<String, Any> = mutableMapOf()

        val kakaoAccountMap = mutableMapOf<String, Any>()
        val email = "sample email"
        kakaoAccountMap["email"] = email
        kakaoAccountMap["is_email_valid"] = true

        attribute["kakao_account"] = kakaoAccountMap
        val kakaoId = "1234"
        attribute[OAuth2Type.KAKAO.oauth2IdName] = kakaoId


        val kakaoProfileMap = mutableMapOf<String, Any>()
        val profileImageUrl = "sample url"
        val nickname = "sample nickname"
        kakaoProfileMap["profile_image_url"] = profileImageUrl
        kakaoProfileMap["nickname"] = nickname

        kakaoAccountMap["profile"] = kakaoProfileMap


        //when
        val member = kakaoToMemberConverter.convert(attribute)

        //then
        with(member) {
            expectThat(name) {
                isEqualTo(nickname)
            }

            expectThat(nickname) {
                isEqualTo(nickname)
            }

            expectThat(email) {
                isEqualTo(email)
            }

            expectThat(profileImagePath) {
                isEqualTo(profileImageUrl)
            }
            expectThat(oauth2LoginId.oauth2Type) {
                isEqualTo(OAuth2Type.KAKAO)
            }
            expectThat(oauth2LoginId.value) {
                isEqualTo(kakaoId)
            }
        }
    }

    @Test
    fun `이메일이 없는 경우 null`() {

        //given
        val attribute: MutableMap<String, Any> = mutableMapOf()

        val kakaoAccountMap = mutableMapOf<String, Any?>()
        val email = "sample email"
        kakaoAccountMap["email"] = null
        kakaoAccountMap["is_email_valid"] = true

        attribute["kakao_account"] = kakaoAccountMap
        val kakaoId = "1234"
        attribute[OAuth2Type.KAKAO.oauth2IdName] = kakaoId


        val kakaoProfileMap = mutableMapOf<String, Any>()
        val profileImageUrl = "sample url"
        val nickname = "sample nickname"
        kakaoProfileMap["profile_image_url"] = profileImageUrl
        kakaoProfileMap["nickname"] = nickname

        kakaoAccountMap["profile"] = kakaoProfileMap


        //when
        val member = kakaoToMemberConverter.convert(attribute)

        //then
        with(member) {
            expectThat(name) {
                isEqualTo(nickname)
            }

            expectThat(member.nickname) {
                isEqualTo(nickname)
            }

            expectThat(member.email) {
                isNull()
            }

            expectThat(profileImagePath) {
                isEqualTo(profileImageUrl)
            }
            expectThat(oauth2LoginId.oauth2Type) {
                isEqualTo(OAuth2Type.KAKAO)
            }
            expectThat(oauth2LoginId.value) {
                isEqualTo(kakaoId)
            }
        }
    }

    @Test
    fun `이메일이 유효하지 않은 경우 null`() {

        //given
        val attribute: MutableMap<String, Any> = mutableMapOf()

        val kakaoAccountMap = mutableMapOf<String, Any?>()
        val email = "sample email"
        kakaoAccountMap["email"] = email
        kakaoAccountMap["is_email_valid"] = false

        attribute["kakao_account"] = kakaoAccountMap
        val kakaoId = "1234"
        attribute[OAuth2Type.KAKAO.oauth2IdName] = kakaoId


        val kakaoProfileMap = mutableMapOf<String, Any>()
        val profileImageUrl = "sample url"
        val nickname = "sample nickname"
        kakaoProfileMap["profile_image_url"] = profileImageUrl
        kakaoProfileMap["nickname"] = nickname

        kakaoAccountMap["profile"] = kakaoProfileMap


        //when
        val member = kakaoToMemberConverter.convert(attribute)

        //then
        with(member) {
            expectThat(name) {
                isEqualTo(nickname)
            }

            expectThat(member.nickname) {
                isEqualTo(nickname)
            }

            expectThat(member.email) {
                isNull()
            }

            expectThat(profileImagePath) {
                isEqualTo(profileImageUrl)
            }
            expectThat(oauth2LoginId.oauth2Type) {
                isEqualTo(OAuth2Type.KAKAO)
            }
            expectThat(oauth2LoginId.value) {
                isEqualTo(kakaoId)
            }
        }
    }

}