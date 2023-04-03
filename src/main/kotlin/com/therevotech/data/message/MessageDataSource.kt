package com.therevotech.data.message

import com.therevotech.data.models.Message

interface MessageDataSource {

    suspend fun getAllMessages(
//        page: Int
    ): List<Message>

    suspend fun insertMessage(message: Message)
}