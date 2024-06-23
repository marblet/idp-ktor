package com.marblet.ktor.idp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class ConsentedScopesTest : StringSpec({
    "canGenerateFromSpaceSeparatedString" {
        val actual = ConsentedScopes.fromSpaceSeparatedString("a b c")

        actual.shouldBe(ConsentedScopes(setOf("a", "b", "c")))
    }

    "canGenerateSpaceSeparatedString" {
        val target = ConsentedScopes(setOf("a", "b", "c"))

        val actual = target.toSpaceSeparatedString()

        actual.shouldBe("a b c")
    }

    "returnTrueIfScopesHasOpenidScope" {
        val target = ConsentedScopes(setOf("openid", "email"))

        val actual = target.hasOpenidScope()

        actual.shouldBeTrue()
    }

    "returnFalseIfNoOpenidScope" {
        val target = ConsentedScopes(setOf("a", "b"))

        val actual = target.hasOpenidScope()

        actual.shouldBeFalse()
    }
})
