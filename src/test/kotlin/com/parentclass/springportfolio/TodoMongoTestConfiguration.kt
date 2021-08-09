package com.parentclass.springportfolio

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Target(AnnotationTarget.CLASS)
@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = [MongoInitializer::class])
annotation class TodoMongoTestConfiguration

class MongoInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(context: ConfigurableApplicationContext) {
        val addedProperties = listOf(
            "spring.data.mongodb.uri=${MongoContainerSingleton.instance.replicaSetUrl}"
        )
        TestPropertyValues.of(addedProperties).applyTo(context.environment)
    }
}

object MongoContainerSingleton {
    val instance: MongoDBContainer by lazy { startMongoContainer() }

    private fun startMongoContainer(): MongoDBContainer =
        MongoDBContainer("mongo:4.2.10")
            .withReuse(true) // remember to put testcontainers.reuse.enable=true into ~/.testcontainers.properties
            .apply { start() }
}