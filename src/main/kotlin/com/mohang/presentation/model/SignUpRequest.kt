package com.mohang.presentation.model

import com.mohang.application.member.usecase.dto.BasicSignUpDto
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

    fun toServiceDto(): BasicSignUpDto {

        //profileImagePath가 null이 아닌 ""가 넘어온 경우 null로 변경하여 넘기기
        if (profileImagePath != null && profileImagePath.isBlank()) {
            return BasicSignUpDto(
                email = email,
                username = username,
                name = name,
                password = password,
                nickname = nickname,
                profileImagePath = null
            )
        }
        return BasicSignUpDto(
            email = email,
            username = username,
            name = name,
            password = password,
            nickname = nickname,
            profileImagePath = profileImagePath
        )
    }
}