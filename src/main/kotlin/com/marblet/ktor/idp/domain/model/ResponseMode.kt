package com.marblet.ktor.idp.domain.model

enum class ResponseMode(val value: String) {
    QUERY("query"),
    FRAGMENT("fragment"),
    ;

    companion object {
        fun from(
            responseMode: String?,
            responseType: ResponseType,
        ): ResponseMode {
            if (responseType.hasToken() || responseType.hasIdToken()) {
                return FRAGMENT
            }
            return if (responseMode == FRAGMENT.value) FRAGMENT else QUERY
        }
    }
}
