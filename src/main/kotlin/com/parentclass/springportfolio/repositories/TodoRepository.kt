package com.parentclass.springportfolio.repositories

import com.parentclass.springportfolio.models.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TodoRepository : MongoRepository<Todo, String> {

    @Query("{todo_tag: ?0, is_deleted: false}")
    fun getTodoByTag(todo_tag: String): Optional<Todo>

    @Query("{is_deleted: false}")
    fun getAllTodos(pageable: Pageable): Page<Todo>
}
