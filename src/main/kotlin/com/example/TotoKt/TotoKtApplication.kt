package com.example.TotoKt

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@EnableR2dbcRepositories
@SpringBootApplication
class TotoKtApplication

fun main(args: Array<String>) {
	runApplication<TotoKtApplication>(*args)

	@Bean
	fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer{
		val initializer = ConnectionFactoryInitializer()
		initializer.setConnectionFactory(connectionFactory)
		val populator = ResourceDatabasePopulator(ClassPathResource("schema.sql"))
		initializer.setDatabasePopulator(populator)
		return initializer
	}

}

