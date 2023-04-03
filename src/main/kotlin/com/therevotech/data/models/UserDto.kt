package com.therevotech.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username: String,
    val imageUrl: String,
    val location: Location?= null
)

@Serializable
data class Location(
    val lat:Double,
    val lng:Double
)

@Serializable
data class Image(
    val imageUrl:String
)

@Serializable
data class Members(
    val members:List<UserDto>
)

@Serializable
data class MessageList(
    val messages:List<Message>
)