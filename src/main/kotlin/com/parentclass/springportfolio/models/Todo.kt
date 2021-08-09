package com.parentclass.springportfolio.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.OffsetDateTime

@Document(collection = "todos")
data class Todo(
    @Id
    val id: ObjectId = ObjectId.get(),
    @Indexed(unique = true)
    val todo_tag: String? = null,
    val content: String,
    val is_deleted: Boolean = false,
    val created_at: OffsetDateTime? = OffsetDateTime.now(),
    val modified_at: OffsetDateTime? = null,
    val deleted_at: OffsetDateTime? = null
)
