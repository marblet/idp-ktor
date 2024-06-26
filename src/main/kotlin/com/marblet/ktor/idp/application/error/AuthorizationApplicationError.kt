package com.marblet.ktor.idp.application.error

import com.marblet.ktor.idp.domain.model.AuthorizationError
import com.marblet.ktor.idp.domain.model.AuthorizationError.CONSENT_REQUIRED

sealed class AuthorizationApplicationError(val error: AuthorizationError, val description: String) {
    data object ClientNotExist : AuthorizationApplicationError(
        AuthorizationError.INVALID_REQUEST,
        "client_id is invalid"
    )

    data object ScopeInvalid : AuthorizationApplicationError(AuthorizationError.INVALID_SCOPE, "scope is invalid.")

    data object RedirectUriInvalid : AuthorizationApplicationError(
        AuthorizationError.INVALID_REQUEST,
        "redirect_uri is invalid."
    )

    data object ResponseTypeInvalid : AuthorizationApplicationError(
        AuthorizationError.UNSUPPORTED_RESPONSE_TYPE,
        "response_type is invalid.",
    )

    data object UserNotFound : AuthorizationApplicationError(AuthorizationError.INVALID_REQUEST, "user not found.")

    data object UserNotAuthenticated : AuthorizationApplicationError(
        AuthorizationError.INVALID_REQUEST,
        "user not logged in."
    )

    data object LoginRequired : AuthorizationApplicationError(AuthorizationError.LOGIN_REQUIRED, "login required.")

    data object ConsentRequired : AuthorizationApplicationError(CONSENT_REQUIRED, "consent required.")
}
