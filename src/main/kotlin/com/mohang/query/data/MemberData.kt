package com.mohang.query.data

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

/**
 * Created by ShinD on 2022/09/06.
 */
data class MemberData @QueryProjection constructor(

    val id: Long, // PK

    var createdAt: LocalDateTime, // 생성 시간

    var modifiedAt: LocalDateTime, // 최종 수정 시간
) {
}