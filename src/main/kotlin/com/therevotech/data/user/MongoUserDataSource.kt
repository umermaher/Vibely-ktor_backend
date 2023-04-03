package com.therevotech.data.user

import com.therevotech.data.models.Location
import com.therevotech.data.models.User
import com.therevotech.data.models.UserDto
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class MongoUserDataSource(
    db:CoroutineDatabase
) : UserDataSource{

    private val users = db.getCollection<User>()

    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::username eq username)
    }

    override suspend fun getUserById(id:String): User? {
        return users.findOne(User::id eq id)
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
                    username = it.username,
                    imageUrl = it.imageUrl,
                    location = it.location
                )
            )
        }
        return usersDto
    }

    override suspend fun updateLocation(userId: String, lat: Double, lng: Double) {
        val loc = Location(lat,lng)
        users.updateOne( User::id eq userId, setValue(User::location,loc))
    }

    override suspend fun updateAvatar(userId: String, imageUrl: String) {
        users.updateOne(User::id eq userId, setValue(User::imageUrl,imageUrl))
    }
}