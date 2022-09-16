package com.mohaeng.configuration.jpa

import com.mohaeng.infrastructure.persistence.Persistence
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Created by ShinD on 2022/08/30.
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackageClasses = [Persistence::class])
class JpaConfiguration {
}