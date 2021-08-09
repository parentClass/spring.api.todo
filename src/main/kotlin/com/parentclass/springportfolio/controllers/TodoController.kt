package com.parentclass.springportfolio.controllers

import com.parentclass.springportfolio.models.Todo
import com.parentclass.springportfolio.repositories.TodoRepository
import com.parentclass.springportfolio.services.TodoTagService
import com.parentclass.springportfolio.utils.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.OffsetDateTime

@RestController
class TodoController @Autowired constructor(
    private val todoRepository: TodoRepository,
    private val todoTagService: TodoTagService
) {

    @ResponseBody
    @RequestMapping(value = ["/api/todos/"], method = [RequestMethod.POST])
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@RequestBody todo: Todo): Todo {
        return todoRepository.save(
            todo.copy(todo_tag = todoTagService.generateRandomTodoTag())
        )
    }

    @RequestMapping(value = ["/api/todos/{todo_tag}"], method = [RequestMethod.GET])
    suspend fun readByTag(@PathVariable todo_tag: String): Todo {
        return todoRepository.getTodoByTag(todo_tag).orElseThrow {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Todo being requested is already deleted."
            )
        }
    }

    @RequestMapping(value = ["/api/todos/"], method = [RequestMethod.GET])
    suspend fun readAll(
        @RequestParam maxResults: Int? = null,
        @RequestParam page: Int? = null
    ): Page<Todo> {
        val pageable = PageRequest.of(
            page ?: Constants.TODO_DEFAULT_PAGE,
            maxResults ?: Constants.TODO_DEFAULT_MAX_RESULT
        )

        return todoRepository.getAllTodos(pageable)
    }

    @PatchMapping("/api/todos/")
    suspend fun update(@RequestBody todo: Todo): Todo {
        val todoTag = requireNotNull(todo.todo_tag)
        val todoToBeUpdated = todoRepository.getTodoByTag(todoTag).orElseThrow {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Todo requested is can't be found."
            )
        }

        return todoRepository.save(
            todoToBeUpdated.copy(
                content = todo.content,
                modified_at = OffsetDateTime.now()
            )
        )
    }

    @DeleteMapping("/api/todos/{todo_tag}")
    suspend fun delete(@PathVariable todo_tag: String): Todo {
        val todoToBeDeleted = todoRepository.getTodoByTag(todo_tag).orElseThrow {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Todo being requested is already deleted."
            )
        }

        return todoRepository.save(
            todoToBeDeleted.copy(is_deleted = true, deleted_at = OffsetDateTime.now())
        )
    }
}
