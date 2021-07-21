package com.example.TotoKt

import com.example.TotoKt.domain.Todo
import com.example.TotoKt.domain.TodoRepository
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier
import java.util.*

//@ExtendWith(SpringExtension::class) //  테스트 클래스임을 설정
@SpringBootTest
@ContextConfiguration(classes = [R2dbcAutoConfiguration::class]) //테스트할 레파지토리 빈을 생성
internal class TodoRepositoryTest(
    @Autowired val todoRepository: TodoRepository,
    @Autowired private val databaseClient: DatabaseClient,
    @Autowired val connectionFactory: ConnectionFactory
) {


    @Test
    fun `test save`() {
        runBlocking {
        val todo = insertTodo()
        todoRepository.save(todo)
            .`as` { StepVerifier.create(it) }
            .expectNextCount(1)
            .verifyComplete()

        val selectOne = databaseClient
            .sql(
                "select count(*) from todos".trimIndent()
            )
            .fetch()
            .one()
        }
    }

    private fun insertTodo(): Todo {
        val givenTodo = generateTodo();
        databaseClient.sql("INSERT INTO todos(content, done) VALUES(:content, :done)")
            .bind("content", UUID.randomUUID().toString())
            .bind("done", 0)
            .fetch()
            .rowsUpdated()
            .block()
        return givenTodo
    }

    private fun generateTodo(): Todo {
        return Todo(
            content = UUID.randomUUID().toString(),
            done = false
        )
    }


}
