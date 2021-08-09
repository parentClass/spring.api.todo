package com.parentclass.springportfolio.services

import com.parentclass.springportfolio.utils.Constants
import org.springframework.stereotype.Service

interface TodoTagService {
    fun generateRandomTodoTag(length: Int = Constants.TODO_TAG_MAX_LENGTH): String
}

@Service
class TodoTagServiceImpl : TodoTagService {
    override fun generateRandomTodoTag(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length).map { allowedChars.random() }.joinToString("")
    }
}
