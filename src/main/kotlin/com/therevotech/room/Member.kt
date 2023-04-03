package com.therevotech.room

import io.ktor.websocket.*

data class Member(
    val username:String,
    val sessionId:String,
    val socket: WebSocketSession,
    val location: Location ?= null
)

data class Location(
    val lat:Long,
    val long:Long
)
