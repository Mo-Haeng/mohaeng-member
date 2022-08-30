package com.mohang.application.usecase.dto

import com.mohang.domain.member.Member

/**
 * Created by ShinD on 2022/08/30.
 */
interface SignUpDto {

    fun toEntity(): Member
}