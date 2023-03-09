package com.therevotech

import com.therevotech.data.user.MongoUserDataSource
import com.therevotech.data.user.User
import com.therevotech.data.user.UserDataSource
import io.ktor.server.application.*
import com.therevotech.plugins.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) : Unit =
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

    GlobalScope.launch {
        val userDataSource = MongoUserDataSource(db)
        val user = User(
            username = "test",
            password = "password",
            salt = "salt"
        )
        userDataSource.insertUser(user)
    }

    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
