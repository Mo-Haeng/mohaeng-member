package com.mohaeng.domain.enums

/**
 * Created by ShinD on 2022/08/30.
 */
enum class Role {

    ADMIN, // 관리자

    BASIC, // 일반 유저

    BLACK, // 블랙리스트

    ;

    val authority: String = "ROLE_${this.name}"

    companion object {
        fun isBlack(role: Role) =
            role == BLACK
    }
}