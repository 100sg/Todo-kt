package com.example.TotoKt.route

import com.example.TotoKt.handler.Todohandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class TodoRouter (private val handler:Todohandler) {

    @Bean
    fun routerFuntion() = nest(path("/todos"),
        router {
            listOf(
                GET("/", handler::getAll),
                GET("/{id}", handler::getById),
                POST("/", handler::save),
                PUT("/{id}/done", handler::done),
                DELETE("/{id}", handler::delete))
        }
    )
}