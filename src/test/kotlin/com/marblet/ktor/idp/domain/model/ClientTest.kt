package com.marblet.ktor.idp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class ClientTest : StringSpec({
    "canVerifyIfClientIsConfidential" {
        val target = Client(ClientId("1"), "secret", setOf(), "name", ClientScopes(setOf()))

        val actual = target.isConfidentialClient()

        actual.shouldBeTrue()
    }

    "canVerifyIfClientIsNotConfidential" {
        val target = Client(ClientId("1"), null, setOf(), "name", ClientScopes(setOf()))

        val actual = target.isConfidentialClient()

        actual.shouldBeFalse()
    }
})

class ClientScopesTest : StringSpec({
    "canGenerateFromSpaceSeparatedString" {
        val actual = ClientScopes.fromSpaceSeparatedString("a b c")

        actual.shouldBe(ClientScopes(setOf("a", "b", "c")))
    }

    "canGenerateSpaceSeparatedString" {
        val target = ClientScopes(setOf("a", "b", "c"))

        val actual = target.toSpaceSeparatedString()

        actual.shouldBe("a b c")
    }
})
