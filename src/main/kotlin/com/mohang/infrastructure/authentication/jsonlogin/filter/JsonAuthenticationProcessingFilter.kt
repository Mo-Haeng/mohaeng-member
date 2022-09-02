package com.mohang.infrastructure.authentication.jsonlogin.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohang.infrastructure.authentication.jsonlogin.authentication.JsonUsernamePasswordToken
import org.springframework.http.HttpMethod.POST
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class JsonAuthenticationProcessingFilter(

    loginUri: String, // 로그인 진행할 uri

    private val objectMapper: ObjectMapper,
) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(loginUri)) {   // 로그인 처리 하지 않을 url 설정

    companion object {

        private const val NO_CONTENT = -1 // 내용이 없는 경우 length는 -1을 반환
    }

    /**
     * 로그인 시도
     */
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        // 요청 메서드와 JSON 형식이 올바른지 체크
        checkMethodAndData(request)

        // request로부터 계정 정보 추출
        val accountDto = extractAccount(request)

        // 공백이 있다면 처리하지 않음
        if (usernameIsBlank(accountDto) || passwordIsBlank(accountDto)) throw AuthException("아이디 혹은 비밀번호가 입력되지 않았습니다.")

        return authenticationManager.authenticate(
            JsonUsernamePasswordToken(
                accountDto.username!!,
                accountDto.password
            )
        );
    }

    /**
     * 메서드 : POST
     * content: Json
     * 컨텐츠가 존재하는지 체크
     */
    private fun checkMethodAndData(request: HttpServletRequest) {
        // 메서드가 Post 가 아닌 경우 로그인 시도하지 않음
        if (!isPost(request)) throw AuthException("로그인은 POST 메서드로 진행해야 합니다.")

        // Json 이 아닌 경우 로그인 시도하지 않음
        if (!isJson(request)) throw AuthException("로그인은 JSON으로 진행해야 합니다.")

        // body에 아무것도 작성되지 않았다면 로그인 실패
        if (request.contentLength == NO_CONTENT) throw AuthException("아이디 혹은 비밀번호가 입력되지 않았습니다.")
    }

    private fun usernameIsBlank(accountDto: AccountDto) =
        accountDto.username == null || accountDto.username!!.isBlank()

    private fun passwordIsBlank(accountDto: AccountDto) =
        accountDto.password == null || accountDto.password!!.isBlank()

    private fun isJson(request: HttpServletRequest) =
        APPLICATION_JSON_VALUE == request.contentType

    private fun isPost(request: HttpServletRequest) =
        POST.name == request.method

    /**
     * Json으로부터 아이디와 비밀번호 추출
     */
    private fun extractAccount(request: HttpServletRequest): AccountDto {
        return try {
            objectMapper.readValue(request.reader, AccountDto::class.java) // Json 파싱 중 오류가 발생할 수 있으므로 예외 처리
        }
        catch (e: Exception) {
            throw AuthException("아이디 혹은 비밀번호 형식이 잘못되었습니다.")
        }
    }

    private data class AccountDto(
        var username: String? = null,
        var password: String? = null,
    )
}