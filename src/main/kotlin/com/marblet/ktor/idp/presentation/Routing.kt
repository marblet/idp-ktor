package com.marblet.ktor.idp.presentation

import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.infrastructure.repository.ClientRepositoryImpl
import com.marblet.ktor.idp.presentation.response.TokenResponse
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/authorize") {
            val client = ClientRepositoryImpl().get(ClientId("test"))
            print(client)
            call.respondRedirect("/consent")
        }
        get("/consent") {
            call.respondText("consent")
        }
        post("/consent") {
            call.respondText("consent")
        }
        post("/token") {
            val formParameters = call.receiveParameters()
            print(formParameters)
            call.respond(
                TokenResponse(
                    accessToken = "access token",
                    tokenType = "Bearer",
                    refreshToken = "refresh token",
                    expiresIn = 3600,
                    idToken = "id token"
                )
            )
        }
        post("/userinfo") {
            call.respondText("userinfo")
        }
    }
}
