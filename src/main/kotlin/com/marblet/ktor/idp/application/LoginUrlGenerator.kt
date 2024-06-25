package com.marblet.ktor.idp.application

import io.ktor.http.URLBuilder

class LoginUrlGenerator {
    fun generate(done: String?): String {
        return URLBuilder(urlString = "http://localhost:8080/login")
            .also { builder -> done?.let { builder.parameters["done"] = it } }
            .buildString()
    }
}
