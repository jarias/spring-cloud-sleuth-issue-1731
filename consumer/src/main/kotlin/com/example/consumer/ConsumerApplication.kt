package com.example.consumer

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class ConsumerApplication

fun main(args: Array<String>) {
    runApplication<ConsumerApplication>(*args)
}

@Configuration
class ConsumerConfig {
    @Bean
    fun consumer(): Consumer<String> {
        return Consumer {
            logger.info { it }
        }
    }
}