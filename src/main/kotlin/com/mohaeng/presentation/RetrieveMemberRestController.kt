package com.mohaeng.presentation

import com.mohaeng.query.dao.MemberDataDao
import com.mohaeng.query.data.MemberData
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * Created by ShinD on 2022/09/06.
 */
@RestController
class RetrieveMemberRestController(

    private val memberDataDao: MemberDataDao,
) {

    private val log = KotlinLogging.logger { }

    /**
     * 회원 정보 조회
     */
    @GetMapping("/api/member/{id}")
    fun retrieveMember(@PathVariable id: Long): ResponseEntity<MemberData> {

        log.info { "회원 정보 조회 요청" }

        return ResponseEntity.ok(memberDataDao.findById(id))
    }
}