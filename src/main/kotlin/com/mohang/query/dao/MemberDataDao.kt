package com.mohang.query.dao

import com.mohang.query.data.MemberData

/**
 * Created by ShinD on 2022/09/06.
 */
interface MemberDataDao {

    fun findById(id: Long): MemberData
}