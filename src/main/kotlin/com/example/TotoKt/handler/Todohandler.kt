package com.example.TotoKt.handler

import com.example.TotoKt.config.Log
import com.example.TotoKt.domain.Todo
import com.example.TotoKt.domain.TodoRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors.toList


@Component
class Todohandler {
    private val repo: TodoRepository
    companion object : Log

    constructor(repo: TodoRepository){
        this.repo = repo
    }

    fun getAll(req: ServerRequest) : Mono<ServerResponse> =
        repo.findAll()
            .filter(Objects::nonNull)
            .collect(toList())
            .flatMap{
                logger.info("call getAll")
                ok().bodyValue(it)}

    fun getById(req: ServerRequest): Mono<ServerResponse> =
        repo.findById(req.pathVariable("id").toLong())
            .flatMap {
                logger.info("call getById : {}", req.pathVariable("id"))
                ok().bodyValue(it) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND)
                .build())

    fun save(req: ServerRequest): Mono<ServerResponse> =
        repo.saveAll(req.bodyToMono(Todo::class.java))
            .flatMap { created(URI.create("/todos/${it.id}"))
                .build() }
            .next()

    fun done(req: ServerRequest): Mono<ServerResponse> =
        repo.findById(req.pathVariable("id").toLong())
            .filter(Objects::nonNull)
            .flatMap { todo ->
                todo.done = true
                todo.modifiedAt = LocalDateTime.now()
                repo.save(todo) }
            .flatMap {
                it?.let { ok().build() }
            }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND)
                .build())

    fun delete(req: ServerRequest): Mono<ServerResponse> =
        repo.findById(req.pathVariable("id").toLong())
            .filter(Objects::nonNull)
            .flatMap { todo ->
                ok().build(repo.deleteById(todo.id!!)) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

}