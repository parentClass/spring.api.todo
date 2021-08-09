package com.parentclass.springportfolio.controllers.todo.implementation

import com.parentclass.springportfolio.TodoTestBase
import com.parentclass.springportfolio.models.Todo
import com.parentclass.springportfolio.utils.MockData
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TodoImplementationTest : TodoTestBase() {

    @Test
    fun `create todo returns created todo given todo object`() = runBlocking {
        val expectedTodo = MockData.todo(
            todo_tag = "random_unique_tag##",
            content = "create todo"
        )

        coEvery { todoTagService.generateRandomTodoTag() } returns "random_unique_tag##"

        val actualTodo = todoController.create(expectedTodo)

        Assertions.assertThat(actualTodo)
            .usingRecursiveComparison()
            .ignoringFields(Todo::created_at.name)
            .isEqualTo(expectedTodo)
        Unit
    }

    @Test
    fun `readByTag todo returns todo for that tag given existing todo tag`() = runBlocking {
        val todos = todoRepository.saveAll(
            listOf(
                MockData.todo(todo_tag = "random_tag#1", content = "todo#1"),
                MockData.todo(todo_tag = "random_tag#2", content = "todo#2")
            )
        )

        val actualTodo = todoController.readByTag(
            requireNotNull(todos.component1().todo_tag)
        )

        Assertions.assertThat(actualTodo)
            .usingRecursiveComparison()
            .ignoringFields(Todo::created_at.name)
            .isEqualTo(todos.component1())
        Unit
    }

    @Test
    fun `readAll todos returns todos`() = runBlocking {
        val expectedTodos = todoRepository.saveAll(
            listOf(
                MockData.todo(todo_tag = "random_tag#1", content = "todo#1"),
                MockData.todo(todo_tag = "random_tag#2", content = "todo#2")
            )
        )

        val actualTodos = todoController.readAll()

        Assertions.assertThat(actualTodos.toList())
            .usingRecursiveComparison()
            .ignoringFields(Todo::created_at.name)
            .isEqualTo(expectedTodos)
        Unit
    }

    @Test
    fun `update todo returns updated todo given todo object`() = runBlocking {
        val savedTodo = todoRepository.save(
            MockData.todo(todo_tag = "random_tag#1", content = "todo#1")
        )

        val expectedTodo = savedTodo.copy(
            content = "updated todo content"
        )

        val actualTodo = todoController.update(expectedTodo)

        Assertions.assertThat(actualTodo)
            .usingRecursiveComparison()
            .ignoringFields(Todo::created_at.name, Todo::modified_at.name)
            .isEqualTo(expectedTodo)
        Unit
    }

    @Test
    fun `delete todo returns deleted todo given todo object and todo tag`() = runBlocking {
        val savedTodo = todoRepository.save(
            MockData.todo(todo_tag = "random_todo_tag", content = "create todo")
        )

        val expectedTodo = savedTodo.copy(
            is_deleted = true
        )

        val actualTodo = todoController.delete(savedTodo.todo_tag!!)

        Assertions.assertThat(actualTodo)
            .usingRecursiveComparison()
            .ignoringFields(Todo::deleted_at.name, Todo::created_at.name)
            .isEqualTo(expectedTodo)
        Unit
    }
}
