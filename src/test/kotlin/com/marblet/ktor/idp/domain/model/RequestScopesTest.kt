package com.marblet.ktor.idp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class RequestScopesTest : StringSpec({

    "canGenerateFromClientScopesWhenScopeNull" {
        val actual = RequestScopes.generate(null, ClientScopes(setOf("a", "b")))

        actual.shouldBe(RequestScopes(setOf("a", "b")))
    }

    "canGenerateFromClientScopesIfRequestValid" {
        val actual = RequestScopes.generate("a b", ClientScopes(setOf("a", "b", "c")))

        actual.shouldBe(RequestScopes(setOf("a", "b")))
    }

    "generateFromClientScopesReturnNullIfRequestInvalid" {
        val actual = RequestScopes.generate("a d", ClientScopes(setOf("a", "b", "c")))

        actual.shouldBeNull()
    }

    "canGenerateFromTokenScopesWhenScopeNull" {
        val actual = RequestScopes.generate(null, TokenScopes(setOf("a", "b")))

        actual.shouldBe(RequestScopes(setOf("a", "b")))
    }

    "canGenerateFromTokenScopesIfRequestValid" {
        val actual = RequestScopes.generate("a b", TokenScopes(setOf("a", "b", "c")))

        actual.shouldBe(RequestScopes(setOf("a", "b")))
    }

    "generateFromTokenScopesReturnNullIfRequestInvalid" {
        val actual = RequestScopes.generate("a d", TokenScopes(setOf("a", "b", "c")))

        actual.shouldBeNull()
    }

    "returnTrueIfScopesHasOpenidScope" {
        val target = RequestScopes(setOf("openid", "email"))

        val actual = target.hasOpenidScope()

        actual.shouldBeTrue()
    }

    "returnFalseIfNoOpenidScope" {
        val target = RequestScopes(setOf("a", "b"))

        val actual = target.hasOpenidScope()

        actual.shouldBeFalse()
    }
})
