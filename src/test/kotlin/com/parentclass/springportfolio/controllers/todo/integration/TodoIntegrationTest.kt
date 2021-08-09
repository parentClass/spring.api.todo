package com.parentclass.springportfolio.controllers.todo.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.parentclass.springportfolio.TodoTestBase
import com.parentclass.springportfolio.utils.Constants
import com.parentclass.springportfolio.utils.MockData
import io.mockk.coEvery
import org.hamcrest.Matchers
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class TodoIntegrationTest : TodoTestBase() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `create should return created todo given valid todo object`() {
        val todo = MockData.todo(
            content = "a mock data for todo test"
        )

        coEvery { todoTagService.generateRandomTodoTag() } returns "random_tag_for_create"

        mockMvc.post("/api/todos/") {
            accept(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todo)
        }.asyncDispatch().andExpect {
            status { isCreated() }
            jsonPath("$.todo_tag", notNullValue())
            jsonPath("$.created_at", notNullValue())
        }
    }

    @Test
    fun `readByTag should return todo given valid todo tag`() {
        // Saved todo
        val savedTodo = todoRepository.save(
            MockData.todo(
                todo_tag = "random_tag_for_read",
                content = "mocked todo"
            )
        )

        mockMvc.get("/api/todos/${savedTodo.todo_tag}") {
            accept(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
        }.asyncDispatch().andExpect {
            status { isOk() }
            jsonPath("$.todo_tag") { value(savedTodo.todo_tag) }
        }
    }

    @Test
    fun `readAll should return paginated todos given maxResult and page`() {
        // Saved todos
        val savedTodos = todoRepository.saveAll(
            listOf(
                MockData.todo(todo_tag = "random_tag_read_all_1", content = "sample todo#1"),
                MockData.todo(todo_tag = "random_tag_read_all_2", content = "sample todo#2"),
                MockData.todo(todo_tag = "random_tag_read_all_3", content = "sample todo#3")
            )
        )

        val maxResults = Constants.TODO_DEFAULT_MAX_RESULT
        val page = Constants.TODO_DEFAULT_PAGE
        val totalElementsCount = savedTodos.size
        val totalPagesCount = ceil((totalElementsCount.toDouble() / maxResults.toDouble())).roundToInt()

        mockMvc.get("/api/todos/?maxResults=$maxResults&page=$page") {
            accept(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
        }.asyncDispatch().andExpect {
            status { isOk() }
            jsonPath("$.totalElements", Matchers.equalTo(totalElementsCount))
            jsonPath("$.totalPages", Matchers.equalTo(totalPagesCount))
            jsonPath("$.pageable.pageNumber", Matchers.equalTo(page))
            jsonPath("$.pageable.pageSize", Matchers.equalTo(maxResults))
        }
    }

    @Test
    fun `update should return updated todo given valid todo object`() {
        // Saved todo
        val savedTodo = todoRepository.save(
            MockData.todo(
                todo_tag = "random_tag_for_update",
                content = "sample todo to be updated"
            )
        )

        // Updated todo
        val updatedTodo = savedTodo.copy(
            content = "updated todo",
            todo_tag = savedTodo.todo_tag
        )

        mockMvc.patch("/api/todos/") {
            accept(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedTodo)
        }.asyncDispatch().andExpect {
            status { isOk() }
            jsonPath("$.content") { value(updatedTodo.content) }
            jsonPath("$.modified_at", notNullValue())
        }
    }

    @Test
    fun `delete should return deleted todo given valid todo tag`() {
        // Saved todo
        val savedTodo = todoRepository.save(
            MockData.todo(
                todo_tag = "random_tag_for_delete",
                content = "sample todo to be deleted"
            )
        )

        mockMvc.delete("/api/todos/${savedTodo.todo_tag}") {
            accept(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
        }.asyncDispatch().andExpect {
            status { isOk() }
            jsonPath("$.is_deleted") { value(true) }
            jsonPath("$.deleted_at", notNullValue())
        }
    }
}
