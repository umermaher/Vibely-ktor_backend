package com.therevotech.plugins

import com.therevotech.authenticate
import com.therevotech.data.user.UserDataSource
import com.therevotech.getSecretInfo
import com.therevotech.security.hashing.HashingService
import com.therevotech.security.hashing.SHA256HashingService
import com.therevotech.security.token.TokenConfig
import com.therevotech.security.token.TokenService
import com.therevotech.signIn
import com.therevotech.signUp
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(hashingService, userDataSource, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}
