package com.mohaeng.application.member.usecase

import com.mohaeng.application.member.exception.DuplicateEmailException
import com.mohaeng.application.member.exception.DuplicateUsernameException
import com.mohaeng.domain.enums.OAuth2Type.NONE
import com.mohaeng.domain.member.MemberPasswordEncoder
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.fixture.MemberFixture.basicSignUpDto
import com.mohaeng.fixture.MemberFixture.notSavedMember
import com.mohaeng.fixture.MemberFixture.savedMember
import com.mohaeng.infrastructure.persistence.MemberRepository
import com.mohaeng.mock.MockTransactionTemplate
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.transaction.support.TransactionTemplate
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo


/**
 * Created by ShinD on 2022/08/30.
 */

//@JdbcTest
//@AutoConfigureJdbc
//@ExtendWith(SpringExtension::class)
class SignUpUseCaseTest {

    private var memberRepository: MemberRepository = mockkClass(MemberRepository::class)

    private val transaction: TransactionTemplate = MockTransactionTemplate()

    private lateinit var signUpUseCase: SignUpUseCase

    @MockK
    private var passwordEncoder: MemberPasswordEncoder = mockkClass(MemberPasswordEncoder::class)

    @BeforeEach
    fun setUp(){
        signUpUseCase = SignUpUseCase(memberRepository, transaction, passwordEncoder)
    }

    @Test
    fun `일반 회원가입`() {

        //given
        val username = "sample username"
        val encodedPw = "{encoded} pw"
        val general = OAuth2LoginId(oauth2Type = NONE, value = username)
        val savedMember = savedMember(oauth2LoginId = general, password = encodedPw)

        val basicSignUpDto = basicSignUpDto(username = username)
        every { memberRepository.findByOauth2LoginId(general) } returns null
        every { passwordEncoder.encode(basicSignUpDto.password) } returns encodedPw
        every { memberRepository.save(any()) } returns savedMember
        every { memberRepository.findByEmail(any()) } returns null




        //when
        expectThat(signUpUseCase.command(basicSignUpDto)) {
            isEqualTo(savedMember.id)
        }

        //then
        verify (exactly = 1){ memberRepository.findByOauth2LoginId(general) }
        verify (exactly = 1){ passwordEncoder.encode(basicSignUpDto.password) }
        verify (exactly = 1){ memberRepository.save(any()) }
    }


    @Test
    fun `일반 회원가입 실패 - 아이디 중복`() {

        //given
        val username = "sample username"
        val general = OAuth2LoginId(oauth2Type = NONE, value = username)

        val basicSignUpDto = basicSignUpDto(username = username)
        every { memberRepository.findByOauth2LoginId(general) } returns notSavedMember()
        every { memberRepository.findByEmail(any()) } returns null

        //when
        expectThrows<DuplicateUsernameException> { signUpUseCase.command(basicSignUpDto) }

        //then
        verify (exactly = 0){ memberRepository.save(any()) }
    }

    @Test
    fun `일반 회원가입 실패 - 이메일 중복`() {

        //given
        val username = "sample username"
        val general = OAuth2LoginId(oauth2Type = NONE, value = username)

        val basicSignUpDto = basicSignUpDto(username = username)
        every { memberRepository.findByOauth2LoginId(general) } returns null
        every { memberRepository.findByEmail(any()) } returns notSavedMember()

        //when
        expectThrows<DuplicateEmailException> { signUpUseCase.command(basicSignUpDto) }

        //then
        verify (exactly = 0){ memberRepository.save(any()) }
    }

    @Test
    @DisplayName("일반 회원가입 실패 - memberRepository.save() 오류 발생")
    fun `일반 회원가입 실패 - save() 오류 발생`() {

        //given
        val username = "sample username"
        val encodedPw = "{encoded} pw"
        val general = OAuth2LoginId(oauth2Type = NONE, value = username)

        val basicSignUpDto = basicSignUpDto(username = username)
        every { memberRepository.findByOauth2LoginId(general) } returns null
        every { passwordEncoder.encode(basicSignUpDto.password) } returns encodedPw
        every { memberRepository.save(any()) } throws RuntimeException()
        every { memberRepository.findByEmail(any()) } returns null

        //when
        expectThrows<RuntimeException> { signUpUseCase.command(basicSignUpDto) }

        //then
        verify (exactly = 1){ memberRepository.save(any()) }
    }
}