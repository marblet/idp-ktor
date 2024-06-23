package com.marblet.ktor.idp.plugins

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    Database.connect(
        url = environment.config.property("exposed.url").getString(),
        user = environment.config.property("exposed.user").getString(),
        password = environment.config.property("exposed.password").getString(),
    )
}
