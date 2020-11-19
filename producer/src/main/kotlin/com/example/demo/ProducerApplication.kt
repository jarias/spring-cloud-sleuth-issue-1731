package com.example.demo

import mu.KotlinLogging
import org.springframework.beans.factory.BeanFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSleuthOperators
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
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
    fun sink(): EmitterProcessor<Message<String>> {
        return EmitterProcessor.create()
    }

    @Bean
    fun producer(): Supplier<Flux<Message<String>>> {
        return Supplier { sink() }
    }

    @Bean
    fun router(beanFactory: BeanFactory) = coRouter {
        POST("/") {
            logger.info { "Sending message" }
            val message = MessageBuilder.withPayload("Hello World").build()
            sink().onNext(MessagingSleuthOperators.handleOutputMessage(beanFactory, MessagingSleuthOperators.forInputMessage(beanFactory, message)))
            ServerResponse.noContent().buildAndAwait()
        }
    }
}
