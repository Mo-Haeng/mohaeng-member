package com.mohaeng.configuration.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by ShinD on 2022/09/06.
 */
@Configuration
class QueryDslConfiguration(

    @PersistenceContext val em: EntityManager,
) {

    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(em)
}