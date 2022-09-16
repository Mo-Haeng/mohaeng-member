package com.mohaeng.query.dao

import com.mohaeng.query.data.MemberData

/**
 * Created by ShinD on 2022/09/06.
 */
interface MemberDataDao {

    fun findById(id: Long): MemberData
}