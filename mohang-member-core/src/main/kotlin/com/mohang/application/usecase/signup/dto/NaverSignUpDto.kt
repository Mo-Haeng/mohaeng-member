package com.mohang.application.usecase.signup.dto

import com.mohang.domain.RegistrationId

/**
 * Created by ShinD on 2022/08/30.
 */
data class NaverSignUpDto(

    // 회원가입 시 사용한 소셜 서비스와, 해당 서비스에서 제공하는 식별값
    var registrationId: RegistrationId,
)
