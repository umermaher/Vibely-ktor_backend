package com.therevotech.data.user

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
}