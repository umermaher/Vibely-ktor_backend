package com.therevotech.data.message

import com.therevotech.data.models.Message
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoMessageDataSource(
    db: CoroutineDatabase
) : MessageDataSource{

    private val messages = db.getCollection<Message>()

    override suspend fun getAllMessages(
//        page: Int
    ): List<Message> {
        return messages.find()
//            .skip(page*10).limit(30)
            .descendingSort(Message::timestamp)
            .toList()
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}