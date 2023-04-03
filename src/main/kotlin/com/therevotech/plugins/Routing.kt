package com.therevotech.plugins

import com.therevotech.data.user.UserDataSource
import com.therevotech.room.RoomController
import com.therevotech.routes.*
import com.therevotech.security.hashing.HashingService
import com.therevotech.security.token.TokenConfig
import com.therevotech.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.java.KoinJavaComponent.inject

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
    roomController: RoomController
) {
    routing {
        signIn(hashingService, userDataSource, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
        chatSocket(roomController)
        getAllMessages(roomController)
        getAllMembers(roomController)
        updateLocation(userDataSource)
        updateAvatar(userDataSource)
    }
}
