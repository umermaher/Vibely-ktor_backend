package com.therevotech.routes

import com.therevotech.data.requests.AuthRequest
import com.therevotech.data.user.Image
import com.therevotech.data.user.Location
import com.therevotech.data.user.UserDataSource
import com.therevotech.room.MemberAlreadyExistException
import com.therevotech.room.RoomController
import com.therevotech.routes.authenticate
import com.therevotech.session.ChatSession
import com.therevotech.utils.LOCATION
import com.therevotech.utils.MEMBERS
import com.therevotech.utils.MESSAGES
import com.therevotech.utils.USER
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController){
    authenticate {
        webSocket("chat-socket") {
            val session = call.sessions.get<ChatSession>()
            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No Session!"))
                return@webSocket
            }
            try {
                roomController.onJoin(
                    username = session.username,
                    sessionId = session.sessionId,
                    socket = this
                )
                incoming.consumeEach { frame: Frame ->
                    if (frame is Frame.Text)
                        roomController.sendMessage(
                            senderUsername = session.username,
                            message = frame.readText()
                        )
                }
            } catch (e: MemberAlreadyExistException) {
                call.respond(HttpStatusCode.Conflict, "User already exist!")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                roomController.tryDisconnect(session.username)
            }
        }
    }
}

fun Route.getAllMessages(roomController: RoomController){
    authenticate {
        get(MESSAGES) {
            call.respond(HttpStatusCode.OK,roomController.getAllMessage())
        }
    }
}

fun Route.getAllMembers(roomController: RoomController){
    authenticate {
        get(MEMBERS) {
            call.respond(HttpStatusCode.OK,roomController.getAllMembers())
        }
    }
}

fun Route.updateLocation(userDataSource: UserDataSource){
    authenticate {
        put(LOCATION) {

            val request = call.receiveOrNull<Location>() ?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)

            if(userId == null){
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            userDataSource.updateLocation(
                userId = userId,
                lat = request.lat,
                lng = request.lng
            )
            call.respond(HttpStatusCode.OK,"Location updated!")
        }
    }
}

fun Route.updateAvatar(userDataSource: UserDataSource){
    authenticate {
        put(USER) {

            val request = call.receiveOrNull<Image>() ?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)

            if(userId == null){
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            userDataSource.updateAvatar(userId,request.imageUrl)
            call.respond(HttpStatusCode.OK,"Avatar updated!")
        }
    }
}