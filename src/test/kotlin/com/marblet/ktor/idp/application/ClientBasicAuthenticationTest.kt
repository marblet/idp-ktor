package com.marblet.ktor.idp.application

import com.marblet.ktor.idp.domain.model.Client
import com.marblet.ktor.idp.domain.model.ClientId
import com.marblet.ktor.idp.domain.model.ClientScopes
import com.marblet.ktor.idp.domain.repository.ClientRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class ClientBasicAuthenticationTest : StringSpec({
    val clientId = ClientId("id")
    val client =
        Client(
            clientId = clientId,
            secret = "secret",
            redirectUris = setOf("url1", "url2"),
            name = "client name",
            scopes = ClientScopes(setOf("scope1", "scope2")),
        )
    val clientRepository = DummyClientRepository(client)
    val target = ClientBasicAuthentication(clientRepository)

    "returnClientWhenAuthenticated" {
        val actual = target.authenticate("Basic aWQ6c2VjcmV0") // id:secret

        actual.shouldBe(client)
    }

    "returnNullWhenClientNotExist" {
        val actual = target.authenticate("Basic aW52YWxpZC1pZDpzZWNyZXQ=") // invalid-id:secret

        actual.shouldBeNull()
    }

    "returnNullWhenSecretDiffers" {
        val target = ClientBasicAuthentication(clientRepository)

        val actual = target.authenticate("Basic aWQ6cGFzc3dvcmQ=") // id:password

        actual.shouldBeNull()
    }

    "ifNotBasicAuthThenReturnNull" {
        val actual = target.authenticate("Invalid header")

        actual.shouldBeNull()
    }
})

class DummyClientRepository(private val client: Client) : ClientRepository {
    override fun get(clientId: ClientId): Client? {
        if (clientId == client.clientId) {
            return client
        }
        return null
    }
}
