package com.mohaeng.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mohaeng.configuration.exception.ExceptionResponse
import com.mohaeng.fixture.MemberFixture
import com.mohaeng.query.dao.MemberDataDao
import com.mohaeng.query.data.MemberData
import com.mohaeng.query.exception.NotFountMemberException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/09/06.
 */
@WebMvcTest(
    RetrieveMemberRestController::class,
)
@AutoConfigureMockMvc(addFilters = false) // Security Filter 적용 X
class RetrieveMemberRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var ctx: WebApplicationContext

    @MockkBean
    private lateinit var memberDataDao: MemberDataDao

    companion object {
        private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
    }

    /**
     * 인코딩 깨짐 문제 해결
     * https://github.com/HomoEfficio/dev-tips/blob/master/Spring%20Test%20MockMvc%EC%9D%98%20%ED%95%9C%EA%B8%80%20%EA%B9%A8%EC%A7%90%20%EC%B2%98%EB%A6%AC.md
     */
    @BeforeEach
    fun encodingSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .build()
    }

    @Test
    fun `회원 조회 성공`() {

        //given
        val memberId = 1L
        val memberData = MemberFixture.memberData()
        every { memberDataDao.findById(memberId) } returns memberData

        //when
        val result = mockMvc.get("/api/member/${memberId}")
            //then
            .andExpect {
                status { isOk() }
            }
            .andReturn()

        val returnMemberData = mapper.readValue(result.response.contentAsString, MemberData::class.java)

        expectThat(returnMemberData) {
            isEqualTo(memberData)
        }
    }

    @Test
    fun `회원 정보가 없는 경우 예외 메세지`() {

        //given
        val memberId = 1L
        every { memberDataDao.findById(memberId) } throws NotFountMemberException()

        //when
        val result = mockMvc.get("/api/member/${memberId}")
            //then
            .andExpect {
                status { isBadRequest() }
            }
            .andReturn()

        val exceptionResponse = mapper.readValue(result.response.contentAsString, ExceptionResponse::class.java)
        println(exceptionResponse.message)
        expectThat(exceptionResponse.message) {
            isEqualTo(NotFountMemberException().message)
        }
    }
}