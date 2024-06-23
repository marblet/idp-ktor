package com.marblet.ktor.idp.domain.service

import com.marblet.ktor.idp.domain.model.IdTokenPayload

interface IdTokenConverter {
    fun encode(payload: IdTokenPayload): String
}
