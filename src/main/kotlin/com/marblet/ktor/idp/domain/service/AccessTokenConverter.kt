package com.marblet.ktor.idp.domain.service

import com.marblet.ktor.idp.domain.model.AccessTokenPayload

interface AccessTokenConverter {
    fun encode(payload: AccessTokenPayload): String

    fun decode(accessToken: String): AccessTokenPayload?
}
