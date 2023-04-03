package com.therevotech.data.user

import com.therevotech.data.message.Message
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db:CoroutineDatabase
) : UserDataSource{

    private val users = db.getCollection<User>()

    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::username eq username)
    }

    override suspend fun insertUser(user: User): Boolean {
        val existedUser = getUserByUsername(user.username)
        if(existedUser != null){
            return false
        }
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun getAllMembers(): List<UserDto> {
        val users = users.find().toList()
        val usersDto = ArrayList<UserDto>()
        users.forEach {
            usersDto.add(
                UserDto(
                    username = it.username, imageUrl = it.imageUrl
                )
            )
        }
        return usersDto
    }
}