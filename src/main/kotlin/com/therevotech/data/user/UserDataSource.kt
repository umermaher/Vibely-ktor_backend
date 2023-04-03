package com.therevotech.data.user

import org.bson.types.ObjectId

interface UserDataSource {
    suspend fun getUserByUsername(username:String): User?
    suspend fun getUserById(id: String): User?
    suspend fun insertUser(user:User): Boolean
    suspend fun getAllMembers(): List<UserDto>

    suspend fun updateLocation(userId: String, lat: Double, lng: Double)

    suspend fun updateAvatar(userId: String, imageUrl: String)
}