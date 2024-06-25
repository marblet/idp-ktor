package com.marblet.ktor.idp.application

import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.RedirectUri
import io.ktor.http.URLBuilder

class ConsentUrlGenerator {
    fun generate(
        clientId: ClientId,
        responseType: String,
        redirectUri: RedirectUri,
        scope: String?,
        state: String?,
        nonce: String?,
        responseMode: String?,
    ): String {
        return URLBuilder("http://localhost:8080/consent")
            .also { builder ->
                builder.parameters["client_id"] = clientId.value
                builder.parameters["response_type"] = responseType
                builder.parameters["redirect_uri"] = redirectUri.value
                scope?.let { builder.parameters["scope"] = it }
                state?.let { builder.parameters["state"] = it }
                nonce?.let { builder.parameters["nonce"] = it }
                responseMode?.let { builder.parameters["response_mode"] = it }
            }.buildString()
    }
}
