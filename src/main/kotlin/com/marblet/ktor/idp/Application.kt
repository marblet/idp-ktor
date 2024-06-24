package com.marblet.ktor.idp

import com.marblet.ktor.idp.plugins.configureDatabases
import com.marblet.ktor.idp.plugins.configureKoin
import com.marblet.ktor.idp.plugins.configureSerialization
import com.marblet.ktor.idp.presentation.configureRouting
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureRouting()
    configureSerialization()
    configureDatabases()
}
