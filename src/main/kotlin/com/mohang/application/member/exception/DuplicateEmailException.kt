package com.mohang.application.member.exception

/**
 * Created by ShinD on 2022/08/30.
 */
class DuplicateEmailException : RuntimeException(
    "이미 가입된 이메일이 있습니다. 다른 이메일을 입력해주세요"
) {
}