package com.mohang.query.dao

import com.mohang.domain.enums.OAuth2Type.NONE
import com.mohang.domain.member.QMember.member
import com.mohang.query.data.MemberData
import com.mohang.query.data.QMemberData
import com.mohang.query.exception.NotFountMemberException
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Created by ShinD on 2022/09/06.
 */
@Repository
@Transactional(readOnly = true)
class MemberDataDaoQuery(

    private val query: JPAQueryFactory,
) : MemberDataDao {

    override fun findById(id: Long): MemberData {

        val memberData = (query.select(
            QMemberData(
                member.id,
                member.role,
                member.oauth2LoginId.oauth2Type,
                member.oauth2LoginId.value, // username이 아닌 경우 지워주는 로직이 필요함
                member.name,
                member.nickname,
                member.email,
                member.profileImagePath,
                member.point,
                member.createdAt.stringValue(),
                member.modifiedAt.stringValue(),
            )
        )
            .from(member)
            .where(member.id.eq(id))
            .fetchOne()
            ?: throw NotFountMemberException())


        // 소셜 Login의 경우 Username이 없음
        if (memberData.oauth2Type == NONE) {
            return memberData
        }

        return memberData.let {
            MemberData(
                id = it.id,
                role = it.role,
                oauth2Type = it.oauth2Type,
                username = null,
                name = it.name,
                nickname = it.nickname,
                email = it.email,
                profileImagePath = it.profileImagePath,
                point = it.point,
                createdAt = it.createdAt,
                modifiedAt = it.modifiedAt
            )
        }
    }
}