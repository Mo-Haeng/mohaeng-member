package com.mohang.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mohang.application.member.usecase.SignUpUseCase
import com.mohang.application.member.usecase.dto.SignUpDto
import com.mohang.fixture.MemberFixture.signUpRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Created by ShinD on 2022/08/30.
 *
 * https://www.baeldung.com/kotlin/mockmvc-kotlin-dsl
 */
@WebMvcTest(
    SignUpRestController::class,
    excludeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [RestControllerAdvice::class]), ]
)
@AutoConfigureMockMvc(addFilters = false) // Security Filter 적용 X
internal class SignUpRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var signUpUseCase: SignUpUseCase<SignUpDto>

    companion object {
        private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    }

    @Test
    fun `일반 회원가입`() {

        //given
        val memberId = 1L
        val sur = signUpRequest()
        every { signUpUseCase.command(any()) } returns memberId


        //when
        mockMvc.post("/api/member") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(sur)
        }
            //then
            .andExpect {
                status { isCreated() }
                header { string("location", "/api/member/${memberId}") }
            }
    }

    @Test
    fun `일반 회원가입 실패 - 필드가 없는 경우`() {

        //given
        val memberId = 1L
        val sur = signUpRequest(email = "")
        every { signUpUseCase.command(any()) } returns memberId


        //when
        mockMvc.post("/api/member") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(sur)
        }
            //then
            .andExpect {
                status { isBadRequest() }
            }
    }
}