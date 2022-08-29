package com.mohang.config.jpa

import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by ShinD on 2022/08/30.
 */
@MappedSuperclass
class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null // PK

    @Column(updatable = false)
    lateinit var createdAt: LocalDateTime // 생성 시간

    lateinit var modifiedAt: LocalDateTime // 최종 수정 시간


    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        createdAt = now
        modifiedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        modifiedAt = LocalDateTime.now()
    }
}