package com.parentclass.springportfolio.utils

import com.parentclass.springportfolio.models.Todo

object MockData {

    fun todo(
        todo_tag: String? = null,
        content: String,
        is_deleted: Boolean = false
    ) = Todo(
        todo_tag = todo_tag,
        content = content,
        is_deleted = is_deleted
    )
}
