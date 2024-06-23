package com.marblet.ktor.idp.domain.repository

import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.Consent
import com.marblet.ktor.idp.domain.model.UserId

interface ConsentRepository {
    fun get(
        userId: UserId,
        clientId: ClientId,
    ): Consent?

    fun upsert(consent: Consent)
}
