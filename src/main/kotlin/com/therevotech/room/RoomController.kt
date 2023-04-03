package com.therevotech.room

import com.therevotech.data.message.Message
import com.therevotech.data.message.MessageDataSource
import com.therevotech.data.user.UserDataSource
import com.therevotech.data.user.UserDto
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource,
    private val userDataSource: UserDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ){
        if(members.containsKey(username))
            throw MemberAlreadyExistException()

        members[username] = Member(
            username = username,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun sendMessage(senderUsername: String, message:String){
        val user = userDataSource.getUserByUsername(senderUsername)
        members.values.forEach { member ->
            val messageEntity = Message(
                text = message,
                username = senderUsername,
                avatar = user?.imageUrl ?: "",
                timestamp = System.currentTimeMillis()
            )
            messageDataSource.insertMessage(messageEntity)
            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessage(): List<Message>{
        return messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(username: String){
        members[username]?.socket?.close()
        if(members.containsKey(username))
            members.remove(username)
    }

    suspend fun getAllMembers():List<UserDto> = userDataSource.getAllMembers()

}

