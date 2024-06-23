package com.marblet.ktor.idp.domain.service

import com.marblet.ktor.idp.domain.model.RefreshTokenPayload

interface RefreshTokenConverter {
    fun encode(payload: RefreshTokenPayload): String

    fun decode(refreshToken: String): RefreshTokenPayload?
}
