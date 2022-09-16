package com.mohaeng.presentation.model

import com.mohaeng.application.member.usecase.dto.SignUpDto
import javax.validation.constraints.NotBlank

/**
 * Created by ShinD on 2022/08/30.
 */
data class SignUpRequest(

    @field:NotBlank val email: String,

    @field:NotBlank val username: String,

    @field:NotBlank val name: String,

    @field:NotBlank val password: String, //TODO : 형식 정규식으로 설정하기?

    @field:NotBlank val nickname: String,

    val profileImagePath: String? = null,
) {

    fun toServiceDto(): SignUpDto {

        //profileImagePath가 null이 아닌 ""가 넘어온 경우 null로 변경하여 넘기기
        if (profileImagePath != null && profileImagePath.isBlank()) {
            return SignUpDto(
                email = email,
                username = username,
                name = name,
                password = password,
                nickname = nickname,
                profileImagePath = null
            )
        }
        return SignUpDto(
            email = email,
            username = username,
            name = name,
            password = password,
            nickname = nickname,
            profileImagePath = profileImagePath
        )
    }
}