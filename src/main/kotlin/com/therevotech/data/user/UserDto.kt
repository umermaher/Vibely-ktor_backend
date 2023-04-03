package com.therevotech.data.user

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