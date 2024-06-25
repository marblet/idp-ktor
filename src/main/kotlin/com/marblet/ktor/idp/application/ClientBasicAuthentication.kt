package com.marblet.ktor.idp.application

import com.marblet.ktor.idp.domain.model.Client
import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.repository.ClientRepository
import java.util.Base64

class ClientBasicAuthentication(private val clientRepository: ClientRepository) {
    fun authenticate(authorizationHeader: String): Client? {
        if (!authorizationHeader.startsWith("Basic ")) {
            return null
        }
        val decodedHeader =
            Base64.getDecoder().decode(authorizationHeader.substring(6).toByteArray())
                .toString(Charsets.UTF_8)
                .split(":")
        if (decodedHeader.size != 2) {
            return null
        }
        val requestClientId = decodedHeader[0]
        val secret = decodedHeader[1]
        val client = clientRepository.get(ClientId(requestClientId)) ?: return null
        if (secret != client.secret) {
            return null
        }
        return client
    }
}
