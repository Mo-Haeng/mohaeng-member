package com.mohang.domain

/**
 * Created by ShinD on 2022/08/30.
 */
import com.mohang.config.jpa.BaseEntity
import com.mohang.domain.enums.Role
import javax.persistence.*
import javax.persistence.EnumType.STRING

@Entity
@Table(name = "MEMBER")
class Member(

    @Enumerated(STRING)
    @Column(length = 10, nullable = false)
    var role: Role, // 권한

    // 회원가입 시 사용한 소셜 서비스와, 해당 서비스에서 제공하는 식별값
    @Embedded
    var registrationId: RegistrationId,

    @Column(name = "name", length = 50, nullable = false)
    var name: String, // 이름

    @Column(name = "email", length = 50, nullable = false)
    var email: String, // 이메일

    @Column(nullable = true)
    var password: String, // 비밀번호(암호화), 소셜 로그인의 경우 존재 X

    @Column(nullable = false)
    var nickname: String, // 닉네임

    var point: Int = 0, // 포인트

    @Column(nullable = true)
    var profileImagePath: String?, // 프로필 사진 경로 (https://~~)

) : BaseEntity() {

    /**
     * 비밀번호 암호화
     */
    fun passwordEncoding(passwordEncoder: MemberPasswordEncoder) {

        this.password = passwordEncoder.encode(password)
    }

}