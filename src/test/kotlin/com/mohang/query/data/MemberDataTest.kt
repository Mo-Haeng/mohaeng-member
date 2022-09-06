package com.mohang.query.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mohang.domain.enums.OAuth2Type
import com.mohang.domain.member.OAuth2LoginId
import com.mohang.fixture.MemberFixture
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/09/06.
 */
internal class MemberDataTest {

    companion object {
        private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        private const val JSON = """
           {
            "id":%s,
            "role":"%s",
            "oauth2Type":"%s",
            "username":"%s",
            "name":"%s",
            "nickname":"%s",
            "email":"%s",
            "profileImagePath":"%s",
            "point":%s,
            "createdAt":"%s",
            "modifiedAt":"%s"
            }
            """
        private const val USERNAME_NULL_JSON = """
           {
            "id":%s,
            "role":"%s",
            "oauth2Type":"%s",
            "username":null,
            "name":"%s",
            "nickname":"%s",
            "email":"%s",
            "profileImagePath":"%s",
            "point":%s,
            "createdAt":"%s",
            "modifiedAt":"%s"
            }
            """    }

    @Test
    fun `MemberData to json`() {

        //given
        val memberData = MemberFixture.memberData()
        val json = mapper.writeValueAsString(memberData)

        //when
        expectThat(json.replace("\t","").replace(" ", "")) {
            isEqualTo(JSON.format(
                memberData.id,
                memberData.role,
                memberData.oauth2Type,
                memberData.username,
                memberData.name,
                memberData.nickname,
                memberData.email,
                memberData.profileImagePath,
                memberData.point,
                memberData.createdAt,
                memberData.modifiedAt
            ).replace("\t", "").replace(" ","").replace("\n",""))
        }
    }
    @Test
    fun `MemberData username이 없는 경우 to json`() {

        //given
        val memberData = MemberFixture.memberData(username = null, oauth2LoginId = OAuth2LoginId(oauth2Type = OAuth2Type.KAKAO, value = "123"))
        val json = mapper.writeValueAsString(memberData)

        //when
        expectThat(json.replace("\t","").replace(" ", "")) {
            isEqualTo(
                USERNAME_NULL_JSON.format(
                memberData.id,
                memberData.role,
                memberData.oauth2Type,
                memberData.name,
                memberData.nickname,
                memberData.email,
                memberData.profileImagePath,
                memberData.point,
                memberData.createdAt,
                memberData.modifiedAt
            ).replace("\t", "").replace(" ","").replace("\n",""))
        }
    }
}