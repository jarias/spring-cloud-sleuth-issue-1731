package com.example.consumer

import mu.KotlinLogging
import org.springframework.beans.factory.BeanFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSleuthOperators
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Consumer
import java.util.function.Function

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class ConsumerApplication

fun main(args: Array<String>) {
    runApplication<ConsumerApplication>(*args)
}

@Configuration
class ConsumerConfig {
    @Bean
    fun consumer(beanFactory: BeanFactory): Function<Flux<Message<String>>, Mono<Void>> {
        return Function { flux ->
            flux.map { message -> MessagingSleuthOperators.forInputMessage(beanFactory, message) }.map { message ->
                MessagingSleuthOperators.withSpanInScope(beanFactory, message, Consumer { logger.info(message.payload) })
            }.then()
        }
    }
}