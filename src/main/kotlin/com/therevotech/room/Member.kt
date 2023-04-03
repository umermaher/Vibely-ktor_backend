package com.therevotech.room

import io.ktor.websocket.*
import kotlinx.serialization.Serializable

data class Member(
    val username:String,
    val sessionId:String,
    val socket: WebSocketSession,
)
