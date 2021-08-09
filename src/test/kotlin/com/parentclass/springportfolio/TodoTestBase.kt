package com.parentclass.springportfolio

import com.ninjasquad.springmockk.MockkBean
import com.parentclass.springportfolio.controllers.TodoController
import com.parentclass.springportfolio.repositories.TodoRepository
import com.parentclass.springportfolio.services.TodoTagService
import org.junit.After
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@TodoMongoTestConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class TodoTestBase {

    @Autowired
    protected lateinit var todoRepository: TodoRepository

    @Autowired
    protected lateinit var todoController: TodoController

    @MockkBean
    protected lateinit var todoTagService: TodoTagService

    @AfterAll
    @BeforeEach
    fun clean() {
        todoRepository.deleteAll()
    }
}
