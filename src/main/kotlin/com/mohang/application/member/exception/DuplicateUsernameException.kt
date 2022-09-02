package com.mohang.application.member.exception

/**
 * Created by ShinD on 2022/08/30.
 */
class DuplicateUsernameException : RuntimeException(
    "이미 존재하는 아이디가 있습니다. 다른 아이디를 입력해주세요"
) {
}