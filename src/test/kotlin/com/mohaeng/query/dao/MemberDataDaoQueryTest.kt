package com.mohaeng.query.dao

import com.mohaeng.configuration.querydsl.QueryDslConfiguration
import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.fixture.MemberFixture.notSavedMember
import com.mohaeng.infrastructure.persistence.MemberRepository
import com.mohaeng.query.exception.NotFountMemberException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import strikt.assertions.message

/**
 * Created by ShinD on 2022/09/06.
 */
@DataJpaTest
@Import(QueryDslConfiguration::class, MemberDataDaoQuery::class)
internal class MemberDataDaoQueryTest {

    @Autowired
    private lateinit var memberDataDao: MemberDataDao

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    fun `findById 조회 성공`() {

        //given
        val notSavedMember = notSavedMember()
        memberRepository.save(notSavedMember)

        //when
        val memberData = memberDataDao.findById(notSavedMember.id!!)

        //then
        with(memberData) {

            expectThat(id) {
                isEqualTo(notSavedMember.id)
            }
            expectThat(oauth2Type) {
                isEqualTo(notSavedMember.oauth2LoginId.oauth2Type)
            }
            expectThat(username) {
                isEqualTo(notSavedMember.oauth2LoginId.value)
            }
            expectThat(nickname) {
                isEqualTo(notSavedMember.nickname)
            }
            expectThat(role) {
                isEqualTo(notSavedMember.role)
            }
            expectThat(name) {
                isEqualTo(notSavedMember.name)
            }
            expectThat(email) {
                isEqualTo(notSavedMember.email)
            }
            expectThat(profileImagePath) {
                isEqualTo(notSavedMember.profileImagePath)
            }
            expectThat(point) {
                isEqualTo(notSavedMember.point)
            }
            expectThat(createdAt) {
                isEqualTo(notSavedMember.createdAt.toString().replace("T", " "))
            }
            expectThat(modifiedAt) {
                isEqualTo(notSavedMember.modifiedAt.toString().replace("T", " "))
            }
        }
    }

    @Test
    fun `OAuth2 로그인의 경우 username이 없이 조회 성공`() {

        //given
        val notSavedMember =
            notSavedMember(oauth2LoginId = OAuth2LoginId(
                oauth2Type = OAuth2Type.KAKAO,
                value = "1234"
            ))
        memberRepository.save(notSavedMember)

        //when
        val memberData = memberDataDao.findById(notSavedMember.id!!)

        //then
        with(memberData) {

            expectThat(id) {
                isEqualTo(notSavedMember.id)
            }
            expectThat(oauth2Type) {
                isEqualTo(notSavedMember.oauth2LoginId.oauth2Type)
            }
            expectThat(username) {
                isNull()
            }
            expectThat(nickname) {
                isEqualTo(notSavedMember.nickname)
            }
            expectThat(role) {
                isEqualTo(notSavedMember.role)
            }
            expectThat(name) {
                isEqualTo(notSavedMember.name)
            }
            expectThat(email) {
                isEqualTo(notSavedMember.email)
            }
            expectThat(profileImagePath) {
                isEqualTo(notSavedMember.profileImagePath)
            }
            expectThat(point) {
                isEqualTo(notSavedMember.point)
            }
            expectThat(createdAt) {
                isEqualTo(notSavedMember.createdAt.toString().replace("T", " "))
            }
            expectThat(modifiedAt) {
                isEqualTo(notSavedMember.modifiedAt.toString().replace("T", " "))
            }
        }
    }
    @Test
    fun `찾는 회원이 없는 경우 조회 실패`() {

        //when
        val notSavedMember =
            notSavedMember(oauth2LoginId = OAuth2LoginId(
                oauth2Type = OAuth2Type.KAKAO,
                value = "1234"
            ))
        //when
        expectThrows<NotFountMemberException> {
            memberDataDao.findById(99999L)
        }.message.isEqualTo(NotFountMemberException().message)
    }
}