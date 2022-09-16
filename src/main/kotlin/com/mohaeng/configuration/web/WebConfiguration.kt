package com.mohaeng.configuration.web

import com.mohaeng.presentation.argumentresolver.auth.AuthMemberArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Created by ShinD on 2022/09/06.
 */
@Configuration
class WebConfiguration(

    private val authMemberArgumentResolver: AuthMemberArgumentResolver,

    ) : WebMvcConfigurer { // WebMvcConfigurationSupport 와의 차이가 뭘까..? -> http://honeymon.io/tech/2018/03/13/spring-boot-mvc-controller.html

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {

        super.addArgumentResolvers(resolvers)

        resolvers.add(authMemberArgumentResolver)
    }
}