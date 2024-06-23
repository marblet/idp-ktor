package com.marblet.ktor.idp.domain.model

import arrow.core.Either
import arrow.core.left
import arrow.core.right

class ValidatedAuthorizationRequest private constructor(
    val client: Client,
    val responseType: ResponseType,
    val requestScopes: RequestScopes,
    val promptSet: PromptSet,
    val user: User?,
    val nonce: String?,
    val responseMode: ResponseMode,
) {
    companion object {
        fun create(
            client: Client?,
            user: User?,
            responseTypeInput: String,
            redirectUri: RedirectUri,
            scope: String?,
            prompt: String?,
            nonce: String?,
            responseMode: String?,
        ): Either<AuthorizationRequestCreateError, ValidatedAuthorizationRequest> {
            if (client == null) {
                return AuthorizationRequestCreateError.ClientNotExist.left()
            }
            val responseType = ResponseType.find(responseTypeInput)
                ?: return AuthorizationRequestCreateError.ResponseTypeInvalid.left()
            if (!client.redirectUris.contains(redirectUri.value)) {
                return AuthorizationRequestCreateError.RedirectUriInvalid.left()
            }
            val requestScopes = RequestScopes.generate(scope, client.scopes)
                ?: return AuthorizationRequestCreateError.ScopeInvalid.left()
            // ref. docs/protocol_and_flow.md
            if (responseType.requiresOpenidScope() && !requestScopes.hasOpenidScope()) {
                return AuthorizationRequestCreateError.ScopeInvalid.left()
            }
            return ValidatedAuthorizationRequest(
                client = client,
                responseType = responseType,
                requestScopes = requestScopes,
                promptSet = PromptSet.from(prompt),
                user = user,
                nonce = nonce,
                responseMode = ResponseMode.from(responseMode, responseType),
            ).right()
        }
    }
}

sealed class AuthorizationRequestCreateError {
    data object ClientNotExist : AuthorizationRequestCreateError()

    data object ResponseTypeInvalid : AuthorizationRequestCreateError()

    data object ScopeInvalid : AuthorizationRequestCreateError()

    data object RedirectUriInvalid : AuthorizationRequestCreateError()
}
