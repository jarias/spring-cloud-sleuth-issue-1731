package com.example.demo

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Supplier

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class ProducerApplication

fun main(args: Array<String>) {
    runApplication<ProducerApplication>(*args)
}

@Configuration
class TestConfig {
    @Bean
    fun sink(): Sinks.Many<String> {
        return Sinks.many().multicast().onBackpressureBuffer()
    }

    @Bean
    fun producer(): Supplier<Flux<String>> {
        return Supplier { sink().asFlux() }
    }

    @Bean
    fun router() = coRouter {
        POST("/") {
            logger.info { "Sending message" }
            sink().tryEmitNext("Hello world")
            ServerResponse.noContent().buildAndAwait()
        }
    }
}
