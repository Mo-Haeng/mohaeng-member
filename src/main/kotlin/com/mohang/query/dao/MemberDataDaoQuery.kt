package com.mohang.query.dao

import com.mohang.domain.member.QMember.member
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

    override fun findById(id: Long) =
        query.select(QMemberData(member.id, member.createdAt, member.modifiedAt))
            .from(member)
            .fetchOne()
            ?: throw NotFountMemberException()
}