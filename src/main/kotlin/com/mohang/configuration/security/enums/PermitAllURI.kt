package com.mohang.configuration.security.enums

import org.springframework.http.HttpMethod

/**
 * Created by ShinD on 2022/08/31.
 */
enum class PermitAllURI(val method: HttpMethod?, val uri: String) {

    // 로그인 URI
    LOGIN(HttpMethod.POST, "/login"),

    // 회원가입 URI
    SIGN_UP(HttpMethod.POST, "/api/member"),

    // 헬스 체크 URI
    HEALTH_CHECK(HttpMethod.GET, "/health-check"),

    // H2 URI
    H2(null, "/h2-console/**"),

    // 예외 URI
    ERROR(null, "/error"),

    // 메인
    MAIN(null, "/"),

    ;
}