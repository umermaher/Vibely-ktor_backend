package com.therevotech.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class User(
    val username:String,
    val password:String,
    val imageUrl: String = "",
    val location: Location?= null,
    val salt :String,
    @BsonId val id:String = ObjectId().toString()
)