package com.marblet.ktor.idp.domain.repository

import com.marblet.ktor.idp.domain.model.Client
import com.marblet.ktor.idp.domain.model.ClientId

interface ClientRepository {
    fun get(clientId: ClientId): Client?
}
