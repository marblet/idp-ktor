package com.marblet.ktor.idp.application

import com.marblet.ktor.idp.domain.model.RedirectUri
import com.marblet.ktor.idp.domain.model.ResponseMode
import com.marblet.ktor.idp.domain.model.ResponseMode.FRAGMENT
import com.marblet.ktor.idp.domain.model.ResponseMode.QUERY
import io.ktor.http.URLBuilder

class ClientCallbackUrlGenerator {
    fun generate(
        redirectUri: RedirectUri,
        code: String?,
        accessToken: String?,
        idToken: String?,
        state: String?,
        responseMode: ResponseMode,
    ): String {
        when (responseMode) {
            QUERY -> {
                return URLBuilder(
                    urlString = redirectUri.value,
                ).also { builder ->
                    code?.let { builder.parameters["code"] = it }
                    state?.let { builder.parameters["state"] = it }
                }.buildString()
            }

            FRAGMENT -> {
                val fragment =
                    listOfNotNull(
                        accessToken?.let { "access_token=$it&token_type=Bearer" },
                        idToken?.let { "id_token=$it" },
                        code?.let { "code=$it" },
                        state?.let { "state=$it" },
                    )
                        .joinToString("&")
                return URLBuilder(urlString = redirectUri.value)
                    .also { it.fragment = fragment }
                    .buildString()
            }
        }
    }
}
