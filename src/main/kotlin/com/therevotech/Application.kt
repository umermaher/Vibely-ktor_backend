package com.therevotech

import com.therevotech.data.message.MessageDataSource
import com.therevotech.data.message.MongoMessageDataSource
import com.therevotech.data.user.MongoUserDataSource
import com.therevotech.data.user.UserDataSource
import com.therevotech.plugins.*
import com.therevotech.room.RoomController
import com.therevotech.security.hashing.SHA256HashingService
import com.therevotech.security.token.JwtTokenService
import com.therevotech.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)


fun Application.module() {
    val mongoPw = System.getenv("CHAT_MONGO_PW")
    val dbName = "revo-chat"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://umerrasheedmahar2:$mongoPw@cluster0.xear8gz.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    val userDataSource: UserDataSource = MongoUserDataSource(db)
    val messageDataSource: MessageDataSource = MongoMessageDataSource(db)

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    val hashingService = SHA256HashingService()

    val roomController = RoomController(
        messageDataSource = messageDataSource,
        userDataSource = userDataSource
    )


    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureSockets()
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig, roomController)
}
